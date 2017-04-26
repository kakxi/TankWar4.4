package com.kak.work;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile
{
	public static final int X_SPEED = 10;
	public static final int Y_SPEED = 10;
	public static final int WIDTH = 12;
	public static final int HEIGHT = 12;

	private int x, y;
	private Direction dir = Direction.STOP;
	private boolean live = true;
	private boolean good;
	private TankClient tc;

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Map<String, Image> imgs = new HashMap<>();

	static
	{
		// 初始化图片，将图片放到map中
		ClassLoader cl = Explode.class.getClassLoader();
		imgs.put("D", tk.getImage(cl.getResource("images/missileD.gif")));
		imgs.put("L", tk.getImage(cl.getResource("images/missileL.gif")));
		imgs.put("LD", tk.getImage(cl.getResource("images/missileLD.gif")));
		imgs.put("LU", tk.getImage(cl.getResource("images/missileLU.gif")));
		imgs.put("R", tk.getImage(cl.getResource("images/missileR.gif")));
		imgs.put("RD", tk.getImage(cl.getResource("images/missileRD.gif")));
		imgs.put("RU", tk.getImage(cl.getResource("images/missileRU.gif")));
		imgs.put("U", tk.getImage(cl.getResource("images/missileU.gif")));
	}

	public Missile(int x, int y, Direction dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, boolean good, Direction dir, TankClient tc)
	{
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}

	public void draw(Graphics g)
	{
		if (!isLive())// 如果子弹死亡，就不画
		{
			tc.bullets.remove(this);
			return;
		}
		// 根据方向画出子弹
		switch (dir)
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

		move();// 每次重画都调用move
	}

	private void move()
	{
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

		// 判断子弹是否出界
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT)
		{
			live = false;
			if (tc != null)
				tc.bullets.remove(this);
		}
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	// 判断子弹是否击中tank
	public boolean hitTank(Tank t)
	{
		// 判断子弹还活着
		// 判断tank还活着
		// 判断tank和子弹不共边
		// 判断是否击中
		if (this.live && t.isLive() && this.good != t.isGood() && this.getRect().intersects(t.getRect()))
		{
			if (t.isGood())// 如果是打在主战坦克上
			{
				t.setLife(t.getLife() - 20);
				if (t.getLife() <= 0)
					t.setLive(false);
			}
			else
			{
				t.setLive(false);
			}
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks)
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

	public boolean hitWall(Wall wall)
	{
		// 判断子弹还活着
		// 判断是否击中
		if (this.live && this.getRect().intersects(wall.getRect()))
		{
			this.live = false;
			// 去掉撞击墙体的爆炸效果
			// Explode e = new Explode(x, y, tc);
			// tc.explodes.add(e);
			return true;
		}
		return false;
	}

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

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public boolean isLive()
	{
		return live;
	}

	public boolean isGood()
	{
		return good;
	}
}
