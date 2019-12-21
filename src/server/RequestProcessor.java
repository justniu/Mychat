package server;

import client.RequestBody;
import common.*;

import javax.xml.crypto.dsig.SignedInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestProcessor implements Runnable{
    private Socket currentClientSocket;  //当前正在请求服务器的客户端Socket

    public RequestProcessor(Socket currentClientSocket){
        this.currentClientSocket = currentClientSocket;
    }

    public void run() {
        boolean flag = true; //是否不间断监听
        try{
            SingleClientIO currentClientIOCache = new SingleClientIO(
                    new ObjectInputStream(currentClientSocket.getInputStream()),
                    new ObjectOutputStream(currentClientSocket.getOutputStream()));
            while(flag){ //不停地读取客户端发过来的请求对象
                //从请求输入流中读取到客户端提交的请求对象
                RequestBody request = (RequestBody) currentClientIOCache.getOis().readObject();
                System.out.println("Server读取了客户端的请求:" + request.getAction());
                String action = request.getAction();   //获取请求中的动作
                switch (action){
                    case "userRegiste": break;
                    case "userLogin": loginProcess(currentClientIOCache, request);break;
                    case "exit": break;
                    case "chat": break;
                    case "shake": break;
                    case "sendFile": break;
                    case "agreeRecFile": break;
                    case "refusRecFile": break;
                }
            }
        }catch(Exception e){
            System.out.println("客户离开: "+currentClientSocket.getInetAddress().toString().substring(1));
        }
    }
//
//    /** 拒绝接收文件 */
//    private void refuseReceiveFile(Request request) throws IOException {
//        FileInfo sendFile = (FileInfo)request.getAttribute("sendFile");
//        Response response = new Response();  //创建一个响应对象
//        response.setType(ResponseType.REFUSERECEIVEFILE);
//        response.setData("sendFile", sendFile);
//        response.setStatus(ResponseStatus.OK);
//        //向请求方的输出流输出响应
//        OnlineClientIOCache ocic = DataBuffer.onlineUserIOCacheMap.get(sendFile.getFromUser().getId());
//        this.sendResponse(ocic, response);
//    }
//
//    /** 同意接收文件 */
//    private void agreeReceiveFile(Request request) throws IOException {
//        FileInfo sendFile = (FileInfo)request.getAttribute("sendFile");
//        //向请求方(发送方)的输出流输出响应
//        Response response = new Response();  //创建一个响应对象
//        response.setType(ResponseType.AGREERECEIVEFILE);
//        response.setData("sendFile", sendFile);
//        response.setStatus(ResponseStatus.OK);
//        OnlineClientIOCache sendIO = DataBuffer.onlineUserIOCacheMap.get(sendFile.getFromUser().getId());
//        this.sendResponse(sendIO, response);
//
//        //向接收方发出接收文件的响应
//        Response response2 = new Response();  //创建一个响应对象
//        response2.setType(ResponseType.RECEIVEFILE);
//        response2.setData("sendFile", sendFile);
//        response2.setStatus(ResponseStatus.OK);
//        OnlineClientIOCache receiveIO = DataBuffer.onlineUserIOCacheMap.get(sendFile.getToUser().getId());
//        this.sendResponse(receiveIO, response2);
//    }
//
//    /** 客户端退出 */
//    public boolean logout(OnlineClientIOCache oio, Request request) throws IOException{
//        System.out.println(currentClientSocket.getInetAddress().getHostAddress()
//                + ":" + currentClientSocket.getPort() + "走了");
//
//        User user = (User)request.getAttribute("user");
//        //把当前上线客户端的IO从Map中删除
//        DataBuffer.onlineUserIOCacheMap.remove(user.getId());
//        //从在线用户缓存Map中删除当前用户
//        DataBuffer.onlineUsersMap.remove(user.getId());
//
//        Response response = new Response();  //创建一个响应对象
//        response.setType(ResponseType.LOGOUT);
//        response.setData("logoutUser", user);
//        oio.getOos().writeObject(response);  //把响应对象往客户端写
//        oio.getOos().flush();
//        currentClientSocket.close();  //关闭这个客户端Socket
//
//        DataBuffer.onlineUserTableModel.remove(user.getId()); //把当前下线用户从在线用户表Model中删除
//        iteratorResponse(response);//通知所有其它在线客户端
//
//        return false;  //断开监听
//    }
//    /** 注册 */
//    public void registe(OnlineClientIOCache oio, Request request) throws IOException {
//        User user = (User)request.getAttribute("user");
//        UserService userService = new UserService();
//        userService.addUser(user);
//
//        Response response = new Response();  //创建一个响应对象
//        response.setStatus(ResponseStatus.OK);
//        response.setData("user", user);
//
//        oio.getOos().writeObject(response);  //把响应对象往客户端写
//        oio.getOos().flush();
//
//        //把新注册用户添加到RegistedUserTableModel中
//        DataBuffer.registedUserTableModel.add(new String[]{
//                String.valueOf(user.getId()),
//                user.getPassword(),
//                user.getNickname(),
//                String.valueOf(user.getSex())
//        });
//    }
//
    /** process login request */
    public void loginProcess(SingleClientIO currentClientIO, RequestBody request) throws IOException {
        String username = (String)request.getAttribute("username");
        String password = (String) request.getAttribute("password");
        UserServices userService = new UserServices();
        DBExecuteStatus b = userService.login(username, password);

        Response response = new Response();  //创建一个响应对象
        switch (b){
            case HAS_LOGGED_IN_ERROR:
                response.setStatus(ResponseStatus.OK);
                response.setData("status", UserStatus.HAS_LOGGED_IN);
                response.setData("msg", "该 用户已经在别处上线了！");
                try {
                    currentClientIO.getOos().writeObject(response);  //把响应对象往客户端写
                    currentClientIO.getOos().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case OK:
                DataBuffer.onlineUsersList.add(username); //添加到在线用户

                //设置在线用户
                response.setData("onlineUsers",
                        new CopyOnWriteArrayList<String>((String[]) DataBuffer.onlineUsersList.toArray()));

                response.setStatus(ResponseStatus.OK);
                response.setData("status", UserStatus.LOGIN_SUCCESS);
                currentClientIO.getOos().writeObject(response);  //把响应对象往客户端写
                currentClientIO.getOos().flush();

                //通知其它用户有人上线了
                Response response2 = new Response();
                response2.setType(ResponseType.LOGIN);
                response2.setData("loginUser", username);
                iteratorResponse(response2);

                //把当前上线的用户IO添加到缓存Map中
                DataBuffer.onlineUserIOCacheMap.put(username, currentClientIO);

                //把当前上线用户添加到OnlineUserTableModel中
                DataBuffer.onlineUserTableModel.add(username);
                break;

            case NO_EXIST_ERROR:
                response.setStatus(ResponseStatus.OK);
                response.setData("status", UserStatus.NO_EXIST);
                currentClientIO.getOos().writeObject(response);
                currentClientIO.getOos().flush();
                break;
        }
    }
//
//    /** 聊天 */
//    public void chat(Request request) throws IOException {
//        Message msg = (Message)request.getAttribute("msg");
//        Response response = new Response();
//        response.setStatus(ResponseStatus.OK);
//        response.setType(ResponseType.CHAT);
//        response.setData("txtMsg", msg);
//
//        if(msg.getToUser() != null){ //私聊:只给私聊的对象返回响应
//            OnlineClientIOCache io = DataBuffer.onlineUserIOCacheMap.get(msg.getToUser().getId());
//            sendResponse(io, response);
//        }else{  //群聊:给除了发消息的所有客户端都返回响应
//            for(Long id : DataBuffer.onlineUserIOCacheMap.keySet()){
//                if(msg.getFromUser().getId() == id ){	continue; }
//                sendResponse(DataBuffer.onlineUserIOCacheMap.get(id), response);
//            }
//        }
//    }
//
    /*广播*/
    public static void board(String str) throws IOException {
        Message msg = new Message();
        msg.setFromUser("admin");
        msg.setSendTime(new Date());

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append(" ").append(df.format(msg.getSendTime())).append(" ");
        sb.append("系统通知\n  "+str+"\n");
        msg.setMessage(sb.toString());

        Response response = new Response();
        response.setStatus(ResponseStatus.OK);
        response.setType(ResponseType.BOARD);
        response.setData("txtMsg", msg);

        for (String name : DataBuffer.onlineUserIOCacheMap.keySet()) {
            sendResponse_sys(DataBuffer.onlineUserIOCacheMap.get(name), response);
        }
    }
//
    /*踢除用户*/
    public static void remove(String user) throws IOException{
        Message msg = new Message();
        msg.setFromUser("admin");
        msg.setSendTime(new Date());
        msg.setToUser(user);

        StringBuffer sb = new StringBuffer();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        sb.append(" ").append(df.format(msg.getSendTime())).append(" ");
        sb.append("系统通知您\n  "+"您被强制下线"+"\n");
        msg.setMessage(sb.toString());

        Response response = new Response();
        response.setStatus(ResponseStatus.OK);
        response.setType(ResponseType.REMOVE);
        response.setData("txtMsg", msg);

        SingleClientIO io = DataBuffer.onlineUserIOCacheMap.get(msg.getToUser());
        sendResponse_sys(io, response);
    }

    /*私信*/
    public static void chat_sys(String str,String user_) throws IOException{
        Message msg = new Message();
        msg.setFromUser("admin");
        msg.setSendTime(new Date());
        msg.setToUser(user_);

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append(" ").append(df.format(msg.getSendTime())).append(" ");
        sb.append("系统通知您\n  "+str+"\n");
        msg.setMessage(sb.toString());

        Response response = new Response();
        response.setStatus(ResponseStatus.OK);
        response.setType(ResponseType.CHAT);
        response.setData("txtMsg", msg);

        SingleClientIO io = DataBuffer.onlineUserIOCacheMap.get(msg.getToUser());
        sendResponse_sys(io, response);
    }
//
//    /** 发送振动 */
//    public void shake(Request request)throws IOException {
//        Message msg = (Message) request.getAttribute("msg");
//
//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//        StringBuffer sb = new StringBuffer();
//        sb.append(" ").append(msg.getFromUser().getNickname())
//                .append("(").append(msg.getFromUser().getId()).append(") ")
//                .append(df.format(msg.getSendTime())).append("\n  给您发送了一个窗口抖动\n");
//        msg.setMessage(sb.toString());
//
//        Response response = new Response();
//        response.setStatus(ResponseStatus.OK);
//        response.setType(ResponseType.SHAKE);
//        response.setData("ShakeMsg", msg);
//
//        OnlineClientIOCache io = DataBuffer.onlineUserIOCacheMap.get(msg.getToUser().getId());
//        sendResponse(io, response);
//    }
//
//    /** 准备发送文件 */
//    public void toSendFile(Request request)throws IOException{
//        Response response = new Response();
//        response.setStatus(ResponseStatus.OK);
//        response.setType(ResponseType.TOSENDFILE);
//        FileInfo sendFile = (FileInfo)request.getAttribute("file");
//        response.setData("sendFile", sendFile);
//        //给文件接收方转发文件发送方的请求
//        OnlineClientIOCache ioCache = DataBuffer.onlineUserIOCacheMap.get(sendFile.getToUser().getId());
//        sendResponse(ioCache, response);
//    }
//
    /** 给所有在线客户都发送响应 */
    private void iteratorResponse(Response response) throws IOException {
        for(SingleClientIO onlineUserIO : DataBuffer.onlineUserIOCacheMap.values()){
            ObjectOutputStream oos = onlineUserIO.getOos();
            oos.writeObject(response);
            oos.flush();
        }
    }
//
//    /** 向指定客户端IO的输出流中输出指定响应 */
//    private void sendResponse(OnlineClientIOCache onlineUserIO, Response response)throws IOException {
//        ObjectOutputStream oos = onlineUserIO.getOos();
//        oos.writeObject(response);
//        oos.flush();
//    }
//
    /** 向指定客户端IO的输出流中输出指定响应 */
    private static void sendResponse_sys(SingleClientIO onlineUserIO, Response response)throws IOException {
        ObjectOutputStream oos = onlineUserIO.getOos();
        oos.writeObject(response);
        oos.flush();
    }
}
