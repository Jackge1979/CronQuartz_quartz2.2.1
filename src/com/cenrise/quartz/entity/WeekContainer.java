package com.cenrise.quartz.entity;

public enum WeekContainer {
	// 定义枚举类的值
	Monday("MON", "周一"), Tuesday("TUE", "周二"), Wednesday("WED", "周三"), Thursday(
			"THU", "周四"), Friday("FRI", "周五"), Saturday("SAT", "周六") {
		@Override
		public boolean isRest() {
			return true;
		}
	},
	Sunday("SUN", "周日") {
		@Override
		public boolean isRest() {
			return true;
		}
	};

	private String abbreviation = "";// 缩写
	private String chineseName = "";// 中文名字

	// 定义自己的构造器
	private WeekContainer(String abbreviation, String chineseName) {
		this.abbreviation = abbreviation;
		this.chineseName = chineseName;
	}

	public String abbreviation() {
		return abbreviation;
	}

	public String getChineseName() {
		return chineseName;
	}

	// 周六和周日应该返回true，此方法在周六和周日的值中被重载
	public boolean isRest() {
		return false;
	}

	// 重载，对它进行稍稍的改动
	@Override
	public String toString() {
		return this.getClass().getName() + "." + this.name();
	}

	public static void main(String[] args) {
		for (WeekContainer week : WeekContainer.values()) {
			System.out.println("-----------------------------------------");
			System.out.println("ordinal():" + week.ordinal());
			System.out.println("name():" + week.name());
			System.out.println("getChineseName():" + week.getChineseName());
			System.out.println("abbreviation():" + week.abbreviation());
			System.out.println("isRest():" + week.isRest());
			System.out.println("toString():" + week);
		}
	}
}
