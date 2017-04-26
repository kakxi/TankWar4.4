package com.kak.work;

import java.awt.*;

public class Explode
{
	private int x, y;
	private boolean live = true;

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] imgs = new Image[11];

	private int step = 0;
	private TankClient tc;

	private static boolean initImg = false;

	static
	{
		// 初始化图片
		ClassLoader cl = Explode.class.getClassLoader();
		for (int i = 0; i < imgs.length; i++)
		{
			imgs[i] = tk.getImage(cl.getResource("images/" + i + ".gif"));
		}
	}

	public Explode(int x, int y, TankClient tc)
	{
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g)
	{
		// 解决第一次击中敌人时未出现爆炸效果的问题
		// 预先画一遍图片，将图片加载到内存中
		if (!initImg)
		{
			for (int i = 0; i < imgs.length; i++)
			{
				g.drawImage(imgs[i], -100, -100, null);
			}
			initImg = true;
		}

		if (!live)
		{
			tc.explodes.remove(this);
			return;
		}
		if (step == imgs.length)
		{
			live = false;
			step = 0;
			return;
		}

		g.drawImage(imgs[step], x, y, null);

		step++;
	}
}
