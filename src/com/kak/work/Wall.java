package com.kak.work;

import java.awt.*;

/**
 * «ΩÃÂ¿‡
 * @author Administrator
 */
public class Wall
{

	private int x, y;
	private int width, height;
	private TankClient tc;

	public Wall(int x, int y, int width, int height, TankClient tc)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tc = tc;
	}

	public void draw(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, width, height);
	}
}
