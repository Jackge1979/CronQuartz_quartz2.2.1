package com.cenrise.quartz.entity;

public enum WeekContainer {
	// ����ö�����ֵ
	Monday("MON", "��һ"), Tuesday("TUE", "�ܶ�"), Wednesday("WED", "����"), Thursday(
			"THU", "����"), Friday("FRI", "����"), Saturday("SAT", "����") {
		@Override
		public boolean isRest() {
			return true;
		}
	},
	Sunday("SUN", "����") {
		@Override
		public boolean isRest() {
			return true;
		}
	};

	private String abbreviation = "";// ��д
	private String chineseName = "";// ��������

	// �����Լ��Ĺ�����
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

	// ����������Ӧ�÷���true���˷��������������յ�ֵ�б�����
	public boolean isRest() {
		return false;
	}

	// ���أ������������ԵĸĶ�
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
