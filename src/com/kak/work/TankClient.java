package com.kak.work;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是坦克游戏的主窗口
 * @version 1.0
 * @author kak
 */
public class TankClient extends Frame
{
	private static final long serialVersionUID = 1L;
	/**
	 * 游戏窗口默认宽度
	 */
	public static final int GAME_WIDTH = 800;
	/**
	 * 游戏窗口默认高度
	 */
	public static final int GAME_HEIGHT = 600;

	/**
	 * 创建一个变量myTank表示主战坦克，传入tank的初始坐标
	 */
	Tank myTank = new Tank(50, 50, this, true);
	/**
	 * 创建一个集合表示所有敌人坦克对象
	 */
	List<Tank> badTanks = new ArrayList<>();
	/**
	 * 创建一个集合，存放所有的子弹随想
	 */
	List<Missile> bullets = new ArrayList<>();
	/**
	 * 创建一个图片对象，用于缓冲图片显示，解决图标闪烁现象
	 */
	Image offScreenImg;
	/**
	 * 创建一个集合对象，用于存储所有爆炸
	 */
	List<Explode> explodes = new ArrayList<>();

	/**
	 * 创建一个集合对象，用于存放墙体
	 */
	List<Wall> walls = new ArrayList<>();

	/**
	 * 创建一个血块对象Blood
	 */
	Blood blood = new Blood(300, 300);

	/**
	 * 重写paint方法，画出一个圆形，表示tank
	 */
	@Override
	public void paint(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("bullet count：" + bullets.size(), 50, 50);// 显示子弹数量
		g.drawString("explode count：" + explodes.size(), 50, 70);// 显示爆炸数量
		g.drawString("badTanks count：" + badTanks.size(), 50, 90);// 显示爆炸数量
		g.drawString("tank life：" + myTank.getLife(), 50, 110);// 显示爆炸数量
		g.setColor(c);

		if (badTanks.size() <= 0)// 敌人死光时，重新生成
		{
			initBadTank(5);
		}

		for (int i = 0; i < bullets.size(); i++)// 画出子弹
		{
			Missile bul = bullets.get(i);
			bul.draw(g);
			bul.hitTanks(badTanks);// 判断打击所有tank
			bul.hitTank(myTank);// 判断自己是否被子弹打中

			bul.hitWalls(walls);
		}

		for (int i = 0; i < explodes.size(); i++)// 画出爆炸
		{
			Explode e = explodes.get(i);
			e.draw(g);
		}

		for (int i = 0; i < badTanks.size(); i++)
		{
			Tank t = badTanks.get(i);
			t.hitWalls(walls);
			t.hitTanks(badTanks);
			t.draw(g);
		}

		for (int i = 0; i < walls.size(); i++)// 画出墙
		{
			Wall w = walls.get(i);
			w.draw(g);
		}

		myTank.hitTanks(badTanks);// 判断myTank有没有撞到敌人
		myTank.draw(g);// 坦克画出自己

		blood.draw(g);
		myTank.eat(blood);
	}

	/**
	 *  使用双缓冲消除图标的闪烁现象：原理是定义一张虚拟图片，绘图到图片上，再更新到窗口频幕
	 */
	@Override
	public void update(Graphics g)
	{
		if (offScreenImg == null)
			offScreenImg = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		Graphics offgraph = offScreenImg.getGraphics();

		// 刷新背景：每更新一次，都重新画一次背景
		Color c = offgraph.getColor();
		offgraph.setColor(Color.BLACK);
		offgraph.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		offgraph.setColor(c);

		paint(offgraph);// 调用窗口绘图，将虚拟图片的绘图对象传入
		g.drawImage(offScreenImg, 0, 0, null);// 将虚拟图片更新到频幕
	}

	/**
	 * main方法 程序入口
	 * @param args
	 */
	public static void main(String[] args)
	{
		TankClient game = new TankClient();
		game.lauchFrame();
	}

	/**
	 * 初始化主窗口信息
	 */
	public void lauchFrame()
	{
		int initTankCount = 10;
		initTankCount = Integer.parseInt(PropertyManager.getProperty("initTankCount"));

		initBadTank(initTankCount);// 初始化十个敌人
		walls.add(new Wall(100, 200, 500, 15, this));
		walls.add(new Wall(100, 400, 300, 15, this));

		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setResizable(false);// 设置不可改变窗口大小
		this.setTitle("TankWar");
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());// 窗口添加键盘监听

		setVisible(true);

		new Thread(new PaintThread()).start();
	}

	/**
	 * 定时重新绘图的线程
	 */
	private class PaintThread implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
				repaint();
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	// 键盘监听器
	private class KeyMonitor extends KeyAdapter
	{
		@Override
		public void keyReleased(KeyEvent e)
		{
			myTank.keyReleased(e);// 坦克根据键盘松开
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			myTank.keyPressed(e);// 坦克根据键盘按下移动
		}
	}

	public void initBadTank(int n)// 初始化地方tank
	{
		for (int i = 0; i < n; i++)// 在界面上初始化n个地方tank
		{
			badTanks.add(new Tank(10 + 50 * i, 500, this, false, Direction.D));
		}
	}
}
