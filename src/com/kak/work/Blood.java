package com.kak.work;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 血块类
 */
public class Blood
{
	/**
	 * 坐标及长宽
	 */
	private int x, y, w, h;

	private int[][] pos = new int[30][2];

	private int step;

	private boolean live = true;

	public Blood(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = h = 15;
		for (int i = 0; i < pos.length; i++)
		{
			pos[i][0] = x + i * 5;
			pos[i][1] = y + i * 5;
		}
	}

	public void draw(Graphics g)
	{
		if (!live)
			return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);

		move();
	}

	public void move()
	{
		step++;
		if (step == pos.length)
		{
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}

	public boolean isLive()
	{
		return live;
	}

	public void setLive(boolean live)
	{
		this.live = live;
	}
}
