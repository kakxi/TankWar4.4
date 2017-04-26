package com.kak.work;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件管理器
 * @version 1.0
 * @author Administrator
 */
public class PropertyManager
{
	private static Properties props = new Properties();

	static
	{
		try
		{
			props.load(PropertyManager.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	private PropertyManager()
	{
	}

	public static String getProperty(String key)
	{
		return props.getProperty(key);
	}
}
