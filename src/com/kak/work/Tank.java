package com.kak.work;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tank
{
	public static final int X_SPEED = 5;
	public static final int Y_SPEED = 5;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final Color GOOD_COLOR = Color.RED;
	public static final Color BAD_COLOR = Color.BLUE;
	public static final int FULL_BLOOD = 100;

	TankClient tc;
	private int x, y;
	private int oldX, oldY;// �ϴ�����
	private boolean good;
	private boolean live = true;
	private BloodBar bb = new BloodBar();

	private int life = FULL_BLOOD;
	/**
	 *  ��¼��������״̬��boolean����
	 */
	private boolean bU = false, bD = false, bL = false, bR = false;

	private static Random random = new Random();

	private int step = random.nextInt(12) + 3;// ����һ��tank�ƶ��Ĳ���

	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.R;

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Map<String, Image> imgs = new HashMap<>();

	static
	{
		// ��ʼ��ͼƬ����ͼƬ�ŵ�map��
		ClassLoader cl = Tank.class.getClassLoader();
		imgs.put("D", tk.getImage(cl.getResource("images/tankD.gif")));
		imgs.put("L", tk.getImage(cl.getResource("images/tankL.gif")));
		imgs.put("LD", tk.getImage(cl.getResource("images/tankLD.gif")));
		imgs.put("LU", tk.getImage(cl.getResource("images/tankLU.gif")));
		imgs.put("R", tk.getImage(cl.getResource("images/tankR.gif")));
		imgs.put("RD", tk.getImage(cl.getResource("images/tankRD.gif")));
		imgs.put("RU", tk.getImage(cl.getResource("images/tankRU.gif")));
		imgs.put("U", tk.getImage(cl.getResource("images/tankU.gif")));
	}

	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
		oldX = this.x;
		oldY = this.y;
	}

	public Tank(int x, int y, TankClient tc)
	{
		this(x, y);
		this.tc = tc;
	}

	public Tank(int x, int y, TankClient tc, boolean good)
	{
		this(x, y, tc);
		this.good = good;
	}

	public Tank(int x, int y, TankClient tc, boolean good, Direction dir)
	{
		this(x, y, tc, good);
		this.dir = dir;
	}

	public boolean isGood()
	{
		return good;
	}

	public void draw(Graphics g)
	{
		if (!isLive())// ���̹�������Ͳ�����
		{
			if (!good)
				tc.badTanks.remove(this);
			return;
		}
		// �ж��Ƿ������tank����ʾ��ͬ��ɫ
		drawTank(g);

		if (good)// �ж������̹�ˣ�����Ѫ��
			bb.draw(g);

		move();// ÿ���ػ�������move
	}

	/**
	 * ����̹��ͼƬ
	 * @param g
	 */
	public void drawTank(Graphics g)
	{
		switch (ptDir)
		{
			case L:
				g.drawImage(imgs.get("L"), x, y, null);
				break;
			case LU:
				g.drawImage(imgs.get("LU"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("U"), x, y, null);
				break;
			case RU:
				g.drawImage(imgs.get("RU"), x, y, null);
				break;
			case R:
				g.drawImage(imgs.get("R"), x, y, null);
				break;
			case RD:
				g.drawImage(imgs.get("RD"), x, y, null);
				break;
			case D:
				g.drawImage(imgs.get("D"), x, y, null);
				break;
			case LD:
				g.drawImage(imgs.get("LD"), x, y, null);
				break;
			default:
				break;
		}
	}

	/**
	 * Tank���ݷ����ƶ�
	 */
	private void move()
	{
		oldX = this.x;
		oldY = this.y;

		switch (dir)
		{
			case L:
				x -= X_SPEED;
				break;
			case LU:
				x -= X_SPEED;
				y -= Y_SPEED;
				break;
			case U:
				y -= Y_SPEED;
				break;
			case RU:
				x += X_SPEED;
				y -= Y_SPEED;
				break;
			case R:
				x += X_SPEED;
				break;
			case RD:
				x += X_SPEED;
				y += Y_SPEED;
				break;
			case D:
				y += Y_SPEED;
				break;
			case LD:
				x -= X_SPEED;
				y += Y_SPEED;
				break;
			case STOP:
				break;
			default:
				break;
		}

		// �����˶����������Ͳ����
		if (dir != Direction.STOP)
			ptDir = dir;

		// �ж�̹�����꣬����̹�˳���
		if (x < 0)
			x = 0;
		if (y < 30)
			y = 30;
		if (x > TankClient.GAME_WIDTH - WIDTH)
			x = TankClient.GAME_WIDTH - WIDTH;
		if (y > TankClient.GAME_HEIGHT - HEIGHT)
			y = TankClient.GAME_HEIGHT - HEIGHT;

		if (!good)
		{
			Direction[] dirs = Direction.values();
			if (step == 0)// ֻ�е�stepΪ0ʱ���ŵ�������
			{
				step = random.nextInt(12) + 3;
				int rn = random.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;

			if (random.nextInt(50) > 47)// �����������40ʱ�ŷ����ӵ�
			{
				this.fire();
			}
		}
	}

	/**
	 * Tank����keyPressed�¼�
	 * @param e
	 */
	public void keyPressed(KeyEvent e)
	{
		// ���ݼ�ֵ������Tank���ƶ�
		int key = e.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_F2:
				if (!live)
				{
					live = true;
					life = FULL_BLOOD;
				}
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break;
			case KeyEvent.VK_LEFT:
				bL = true;
				break;
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			default:
				break;
		}
		locateDirection();
	}

	public void keyReleased(KeyEvent e)
	{
		// ���ݼ�ֵ������Tank���ƶ�
		int key = e.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_CONTROL:
				fire();
				break;
			case KeyEvent.VK_A:
				superFire();
				break;
			case KeyEvent.VK_UP:
				bU = false;
				break;
			case KeyEvent.VK_DOWN:
				bD = false;
				break;
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			default:
				break;
		}
		locateDirection();
	}

	/**
	 * ���ݰ���ȷ��tank���ƶ�����
	 */
	private void locateDirection()
	{
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;
	}

	private Missile fire()
	{
		if (!isLive())
			return null;
		int x = this.x + (WIDTH - Missile.WIDTH) / 2;
		int y = this.y + (HEIGHT - Missile.HEIGHT) / 2;
		Missile bul = new Missile(x, y, good, ptDir, tc);
		tc.bullets.add(bul);// ����ڵ�
		return bul;
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * �ж�tankײ��tank
	 * @param t Tank����
	 * @return �Ƿ�ײ��
	 */
	public boolean hitTank(Tank t)
	{
		if (this != t)
		{
			// �ж�����Ǵ��״̬
			// �жϵз��Ǵ��
			// �ж��Ƿ���ײ
			if (this.live && t.isLive() && this.getRect().intersects(t.getRect()))
			{
				if (this.good != t.good)
				{
					this.live = false;
					t.setLive(false);
					Explode e = new Explode(x, y, tc);
					tc.explodes.add(e);
					return true;
				}
				else
				{
					this.stay();
				}
			}
		}
		return false;
	}

	/**
	 * �ж��Ƿ�ײ�������ез�Tank
	 * @param tanks Tank���϶���
	 * @return �����Ƿ�ײ���ɹ�
	 */
	public boolean hitTanks(List<Tank> tanks)// tankײ��tank
	{
		for (int i = 0; i < tanks.size(); i++)
		{
			if (hitTank(tanks.get(i)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж�ײ��ǽ�Ĵ���
	 * @param wall ����ǽ�����
	 * @return
	 */
	public boolean hitWall(Wall wall)
	{
		// �ж�tank�Ǵ��״̬
		// �ж��Ƿ���ײ
		if (this.live && this.getRect().intersects(wall.getRect()))
		{
			// ��������Ϊ�ϴ�����
			this.stay();
			return true;
		}
		return false;

	}

	/**
	 * һ���ж�ײ������ǽ�Ĵ���
	 * @param walls
	 * @return
	 */
	public boolean hitWalls(List<Wall> walls)
	{
		for (int i = 0; i < walls.size(); i++)
		{
			if (hitWall(walls.get(i)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж�Tank���״̬
	 * @return
	 */
	public boolean isLive()
	{
		return live;
	}

	public void setLive(boolean live)
	{
		this.live = live;
	}

	/**
	 * ��Tank��������Ϊ�ϴ�ͣ�����
	 */
	private void stay()
	{
		this.x = oldX;
		this.y = oldY;
	}

	private Missile fire(Direction dir)
	{
		if (!isLive())
			return null;
		int x = this.x + (WIDTH - Missile.WIDTH) / 2;
		int y = this.y + (HEIGHT - Missile.HEIGHT) / 2;
		Missile bul = new Missile(x, y, good, dir, tc);
		tc.bullets.add(bul);// ����ڵ�
		return bul;
	}

	// ���䳬���ڵ�����8��������
	private void superFire()
	{
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length - 1; i++)
		{
			fire(dirs[i]);
		}
	}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	// ��ʾѪ��
	private class BloodBar
	{
		public void draw(Graphics g)
		{
			int width = WIDTH;
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, width, 10);
			g.fillRect(x, y - 10, width * life / FULL_BLOOD, 10);
			g.setColor(c);
		}
	}

	public boolean eat(Blood blood)
	{
		if (this.live && blood.isLive() && this.getRect().intersects(blood.getRect()))
		{
			this.setLife(FULL_BLOOD);
			blood.setLive(false);
			return true;
		}
		return false;

	}
}
