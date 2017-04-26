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
 * �������̹����Ϸ��������
 * @version 1.0
 * @author kak
 */
public class TankClient extends Frame
{
	private static final long serialVersionUID = 1L;
	/**
	 * ��Ϸ����Ĭ�Ͽ��
	 */
	public static final int GAME_WIDTH = 800;
	/**
	 * ��Ϸ����Ĭ�ϸ߶�
	 */
	public static final int GAME_HEIGHT = 600;

	/**
	 * ����һ������myTank��ʾ��ս̹�ˣ�����tank�ĳ�ʼ����
	 */
	Tank myTank = new Tank(50, 50, this, true);
	/**
	 * ����һ�����ϱ�ʾ���е���̹�˶���
	 */
	List<Tank> badTanks = new ArrayList<>();
	/**
	 * ����һ�����ϣ�������е��ӵ�����
	 */
	List<Missile> bullets = new ArrayList<>();
	/**
	 * ����һ��ͼƬ�������ڻ���ͼƬ��ʾ�����ͼ����˸����
	 */
	Image offScreenImg;
	/**
	 * ����һ�����϶������ڴ洢���б�ը
	 */
	List<Explode> explodes = new ArrayList<>();

	/**
	 * ����һ�����϶������ڴ��ǽ��
	 */
	List<Wall> walls = new ArrayList<>();

	/**
	 * ����һ��Ѫ�����Blood
	 */
	Blood blood = new Blood(300, 300);

	/**
	 * ��дpaint����������һ��Բ�Σ���ʾtank
	 */
	@Override
	public void paint(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("bullet count��" + bullets.size(), 50, 50);// ��ʾ�ӵ�����
		g.drawString("explode count��" + explodes.size(), 50, 70);// ��ʾ��ը����
		g.drawString("badTanks count��" + badTanks.size(), 50, 90);// ��ʾ��ը����
		g.drawString("tank life��" + myTank.getLife(), 50, 110);// ��ʾ��ը����
		g.setColor(c);

		if (badTanks.size() <= 0)// ��������ʱ����������
		{
			initBadTank(5);
		}

		for (int i = 0; i < bullets.size(); i++)// �����ӵ�
		{
			Missile bul = bullets.get(i);
			bul.draw(g);
			bul.hitTanks(badTanks);// �жϴ������tank
			bul.hitTank(myTank);// �ж��Լ��Ƿ��ӵ�����

			bul.hitWalls(walls);
		}

		for (int i = 0; i < explodes.size(); i++)// ������ը
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

		for (int i = 0; i < walls.size(); i++)// ����ǽ
		{
			Wall w = walls.get(i);
			w.draw(g);
		}

		myTank.hitTanks(badTanks);// �ж�myTank��û��ײ������
		myTank.draw(g);// ̹�˻����Լ�

		blood.draw(g);
		myTank.eat(blood);
	}

	/**
	 *  ʹ��˫��������ͼ�����˸����ԭ���Ƕ���һ������ͼƬ����ͼ��ͼƬ�ϣ��ٸ��µ�����ƵĻ
	 */
	@Override
	public void update(Graphics g)
	{
		if (offScreenImg == null)
			offScreenImg = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		Graphics offgraph = offScreenImg.getGraphics();

		// ˢ�±�����ÿ����һ�Σ������»�һ�α���
		Color c = offgraph.getColor();
		offgraph.setColor(Color.BLACK);
		offgraph.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		offgraph.setColor(c);

		paint(offgraph);// ���ô��ڻ�ͼ��������ͼƬ�Ļ�ͼ������
		g.drawImage(offScreenImg, 0, 0, null);// ������ͼƬ���µ�ƵĻ
	}

	/**
	 * main���� �������
	 * @param args
	 */
	public static void main(String[] args)
	{
		TankClient game = new TankClient();
		game.lauchFrame();
	}

	/**
	 * ��ʼ����������Ϣ
	 */
	public void lauchFrame()
	{
		int initTankCount = 10;
		initTankCount = Integer.parseInt(PropertyManager.getProperty("initTankCount"));

		initBadTank(initTankCount);// ��ʼ��ʮ������
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
		this.setResizable(false);// ���ò��ɸı䴰�ڴ�С
		this.setTitle("TankWar");
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());// ������Ӽ��̼���

		setVisible(true);

		new Thread(new PaintThread()).start();
	}

	/**
	 * ��ʱ���»�ͼ���߳�
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

	// ���̼�����
	private class KeyMonitor extends KeyAdapter
	{
		@Override
		public void keyReleased(KeyEvent e)
		{
			myTank.keyReleased(e);// ̹�˸��ݼ����ɿ�
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			myTank.keyPressed(e);// ̹�˸��ݼ��̰����ƶ�
		}
	}

	public void initBadTank(int n)// ��ʼ���ط�tank
	{
		for (int i = 0; i < n; i++)// �ڽ����ϳ�ʼ��n���ط�tank
		{
			badTanks.add(new Tank(10 + 50 * i, 500, this, false, Direction.D));
		}
	}
}
