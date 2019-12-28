package client;

import common.Message;
import common.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ClientChatFrame extends JFrame {
	/** 当前用户信息Lbl */
	private JLabel currentUserLbl;
	/**聊天信息列表区域*/
	public static JTextArea msgListArea;
	/**要发送的信息区域*/
	public static JTextArea sendArea;
	/** 在线用户列表 */
	public static JList onlineList;
	/** 在线用户数统计Lbl */
	public static JLabel onlineCountLbl;
	/** 准备发送的文件 */
//	public static FileInfo sendFile;

	/** 私聊复选框 */
	public JButton userListBtn;
	public JCheckBox rybqBtn;

	public ClientChatFrame(){
		this.init();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	public void init(){
		this.setTitle("聊天室");
		this.setSize(550, 500);
		this.setResizable(false);

		this.setLocationRelativeTo(null);

		//左边主面板
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		//右边用户面板
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BorderLayout());

		// 创建一个分隔窗格
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				mainPanel, userPanel);
		splitPane.setDividerLocation(550);
		splitPane.setDividerSize(0);
		splitPane.getRightComponent().setVisible(false);
		this.add(splitPane, BorderLayout.CENTER);

		//左上边信息显示面板
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		//右下连发送消息面板
		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new BorderLayout());

		// 创建一个分隔窗格
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				infoPanel, sendPanel);
		splitPane2.setDividerLocation(300);
		splitPane2.setDividerSize(1);
		mainPanel.add(splitPane2, BorderLayout.CENTER);

		/**聊天对方的信息Label*/
		JLabel otherInfoLbl = new JLabel("当前状态：群聊中...");
		infoPanel.add(otherInfoLbl, BorderLayout.NORTH);

		msgListArea = new JTextArea();
		msgListArea.setLineWrap(true);
		infoPanel.add(new JScrollPane(msgListArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BorderLayout());
		sendPanel.add(tempPanel, BorderLayout.NORTH);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tempPanel.add(btnPanel, BorderLayout.CENTER);

		//发送文件按钮
		JButton sendFileBtn = new JButton(new ImageIcon("images/sendPic.png"));
		sendFileBtn.setMargin(new Insets(0,0,0,0));
		sendFileBtn.setToolTipText("向对方发送文件");
		btnPanel.add(sendFileBtn);

		//私聊按钮
		rybqBtn = new JCheckBox("私聊");
		btnPanel.add(rybqBtn);

		//用户列表
		userListBtn = new JButton("用户列表");
		tempPanel.add(userListBtn, BorderLayout.EAST);

		//要发送的信息的区域
		sendArea = new JTextArea();
		sendArea.setLineWrap(true);
		sendPanel.add(new JScrollPane(sendArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		// 聊天按钮面板
		JPanel btn2Panel = new JPanel();
		btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton closeBtn = new JButton("关闭");
		closeBtn.setToolTipText("退出整个程序");
		btn2Panel.add(closeBtn);
		JButton submitBtn = new JButton("发送");
		submitBtn.setToolTipText("按Enter键发送消息");
		btn2Panel.add(submitBtn);
		sendPanel.add(btn2Panel, BorderLayout.SOUTH);

		//在线用户列表面板
		JPanel onlineListPane = new JPanel();
		onlineListPane.setLayout(new BorderLayout());
		onlineCountLbl = new JLabel("在线用户列表(1)");
		onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);

		//当前用户面板
		JPanel currentUserPane = new JPanel();
		currentUserPane.setLayout(new BorderLayout());
		currentUserPane.add(new JLabel("当前用户"), BorderLayout.NORTH);

		// 右边用户列表创建一个分隔窗格
		JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				onlineListPane, currentUserPane);
		splitPane3.setDividerLocation(340);
		splitPane3.setDividerSize(1);
		userPanel.add(splitPane3, BorderLayout.CENTER);

		//获取在线用户并缓存
		ConManager.onlineUserListModel = new OnlineUserListModel(ConManager.onlineUsers);
		//在线用户列表
		onlineList = new JList(ConManager.onlineUserListModel);
		onlineList.setCellRenderer(new MyCellRenderer());
		//设置为单选模式
		onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		onlineListPane.add(new JScrollPane(onlineList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		//当前用户信息Label
		currentUserLbl = new JLabel();
		currentUserPane.add(currentUserLbl);

		///////////////////////注册事件监听器/////////////////////////
		//关闭窗口
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				logout();
			}
		});

		//关闭按钮的事件
		closeBtn.addActionListener(event -> logout());

		userListBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(splitPane.getRightComponent().isVisible()){
					splitPane.setDividerSize(0);
					splitPane.setDividerLocation(550);
					splitPane.getRightComponent().setVisible(false);
				}else{
					splitPane.getRightComponent().setVisible(true);
					splitPane.setDividerLocation(400);
					splitPane.setDividerSize(10);
				}
			}
		});

		//选择某个用户私聊
		rybqBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(rybqBtn.isSelected()){
					String selectedUser = (String)onlineList.getSelectedValue();
					if(null == selectedUser){
						otherInfoLbl.setText("当前状态：私聊(选中在线用户列表中某个用户进行私聊)...");
					}else if(ConManager.currentUser.equals(selectedUser)){
						otherInfoLbl.setText("当前状态：想自言自语?...系统不允许");
					}else{
						otherInfoLbl.setText("当前状态：与 "+ selectedUser +" 私聊中...");
					}
				}else{
					otherInfoLbl.setText("当前状态：群聊...");
				}
			}
		});

//		//选择某个用户Jlist
//		onlineList.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				User selectedUser = (User)onlineList.getSelectedValue();
//				if(rybqBtn.isSelected()){
//					if(ConManager.currentUser.getId() == selectedUser.getId()){
//						otherInfoLbl.setText("当前状态：想自言自语?...系统不允许");
//					}else{
//						otherInfoLbl.setText("当前状态：与 "+ selectedUser.getNickname()
//								+"(" + selectedUser.getId() + ") 私聊中...");
//					}
//				}
//			}
//		});

		//发送文本消息
		sendArea.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == Event.ENTER){
					sendTxtMsg();
				}
			}
		});
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendTxtMsg();
			}
		});

//		//发送文件
//		sendFileBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//				sendFile();
//			}
//		});
//
		this.loadData();  //加载初始数据
	}

	/**  加载数据 */
	public void loadData(){
		//加载当前用户数据
		if(null != ConManager.currentUser){
			currentUserLbl.setIcon(
					new ImageIcon("images/head.png"));
			currentUserLbl.setText(ConManager.currentUser);
		}
		//设置在线用户列表
		onlineCountLbl.setText("在线用户列表("+ ConManager.onlineUserListModel.getSize() +")");
		//启动监听服务器消息的线程
		new ClientThread(this).start();
	}


	/** 发送文本消息 */
	public void sendTxtMsg() {
		String content = sendArea.getText();
		if ("".equals(content)) { //无内容
			JOptionPane.showMessageDialog(ClientChatFrame.this, "不能发送空消息!",
					"不能发送", JOptionPane.ERROR_MESSAGE);
		} else { //发送
			String selectedUser = (String) onlineList.getSelectedValue();
			if (!"".equals(selectedUser) &&
					ConManager.currentUser.equals(selectedUser)) {
				JOptionPane.showMessageDialog(ClientChatFrame.this, "不能给自己发送消息!",
						"不能发送", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//如果设置了ToUser表示私聊，否则群聊
			Message msg = new Message();
			if (rybqBtn.isSelected()) {  //私聊
				if (null == selectedUser) {
					JOptionPane.showMessageDialog(ClientChatFrame.this, "没有选择私聊对象!",
							"不能发送", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (ConManager.currentUser.equals(selectedUser)) {
					JOptionPane.showMessageDialog(ClientChatFrame.this, "不能给自己发送消息!",
							"不能发送", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					msg.setToUser(selectedUser);
				}
			}
			msg.setFromUser(ConManager.currentUser);
			msg.setSendTime(new Date());

			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			StringBuffer sb = new StringBuffer();
			sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
					.append(msg.getFromUser());
			if (!this.rybqBtn.isSelected()) {//群聊
				sb.append("对大家说");
			}
			sb.append("\n  ").append(content).append("\n");
			msg.setMessage(sb.toString());

			RequestBody request = new RequestBody();
			request.setAction("chat");
			request.setAttribute("msg", msg);
			try {
				Requests.sendTextRequestOnly(request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			//JTextArea中按“Enter”时，清空内容并回到首行
			InputMap inputMap = sendArea.getInputMap();
			ActionMap actionMap = sendArea.getActionMap();
			Object transferTextActionKey = "TRANSFER_TEXT";
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);
			actionMap.put(transferTextActionKey, new AbstractAction() {
				private static final long serialVersionUID = 7041841945830590229L;

				public void actionPerformed(ActionEvent e) {
					sendArea.setText("");
					sendArea.requestFocus();
				}
			});
			sendArea.setText("");
			//    /** 把指定文本添加到消息列表文本域中 */
			Requests.appendTxt2MsgListArea(msg.getMessage());
		}
	}

//	/** 发送文件 */
//	private void sendFile() {
//		User selectedUser = (User)onlineList.getSelectedValue();
//		if(null != selectedUser){
//			if(ConManager.currentUser.getId() == selectedUser.getId()){
//				JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送文件!",
//						"不能发送", JOptionPane.ERROR_MESSAGE);
//			}else{
//				JFileChooser jfc = new JFileChooser();
//				if (jfc.showOpenDialog(ChatFrame.this) == JFileChooser.APPROVE_OPTION) {
//					File file = jfc.getSelectedFile();
//					sendFile = new FileInfo();
//					sendFile.setFromUser(ConManager.currentUser);
//					sendFile.setToUser(selectedUser);
//					try {
//						sendFile.setSrcName(file.getCanonicalPath());
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					sendFile.setSendTime(new Date());
//
//					Request request = new Request();
//					request.setAction("toSendFile");
//					request.setAttribute("file", sendFile);
//					try {
//						ClientUtil.sendTextRequest2(request);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//					ClientUtil.appendTxt2MsgListArea("【文件消息】向 "
//							+ selectedUser.getNickname() + "("
//							+ selectedUser.getId() + ") 发送文件 ["
//							+ file.getName() + "]，等待对方接收...\n");
//				}
//			}
//		}else{
//			JOptionPane.showMessageDialog(ChatFrame.this, "不能给所有在线用户发送文件!",
//					"不能发送", JOptionPane.ERROR_MESSAGE);
//		}
//	}

	/** 关闭客户端 */
	private void logout(){
		int select = JOptionPane.showConfirmDialog(ClientChatFrame.this,
				"确定退出吗？\n\n退出程序将中断与服务器的连接!", "退出聊天室",
				JOptionPane.YES_NO_OPTION);
		if (select == JOptionPane.YES_OPTION) {
			RequestBody req = new RequestBody();
			req.setAction("exit");
			req.setAttribute("user", ConManager.currentUser);
			try {
				Requests.sendTextRequest(req);
			} catch (IOException ex) {
				ex.printStackTrace();
			}finally{
				System.exit(0);
			}
		}else{
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
	}

	public static void main(String[] args) {
		new ClientChatFrame();
	}
}

