package test.com.cenrise.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.quartz.CronExpression;

import com.cenrise.quartz.util.DateFormatUtil;

public class CronTransUtil1 {

	private static CTabFolder CTabFolder;
	private static Button button;

	private Map<Integer, Button> s_select_map = new TreeMap<Integer, Button>();// 秒选择
	private Map<Integer, Button> min_select_map = new TreeMap<Integer, Button>();// 分选择
	private Map<Integer, Button> h_select_map = new TreeMap<Integer, Button>();// 时选择
	private Map<Integer, Button> d_select_map = new TreeMap<Integer, Button>();// 天选择
	private Map<Integer, Button> w_select_map = new TreeMap<Integer, Button>();// 周选择
	private Map<Integer, Button> m_select_map = new TreeMap<Integer, Button>();// 月选择
	// 按钮
	private Button sTriggerButton, sCycleButton, sSpecifyButton;
	private CCombo sComboEvery, sComboStart;
	private Button minTriggerButton, minCycleButton, minSpecifyButton;
	private CCombo minComboEvery, minComboStart;
	private Button hTriggerButton, hSpecifyButton;
	private Button dTriggerButton, dSpecifyButton;
	private Button mTriggerButton, mSpecifyButton;
	private Button wConfigurationButton, wTriggerButton, wSpecifyButton;
	private Map<String, String> dateData = new HashMap<String, String>();

	private Text expressionText;// 生成的表达式显示框
	private Text startTimeText;// 开始时间
	private Text performTimeText;// 执行时间

	private Shell shell;
	private Display display;
	private String cronExpressionReturn;// open的返回值
	static Text performTimeText2;// 存入返回的值

	/**
	 * 执行
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell2 = new Shell(display);
		shell2.setText("Shell");
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = 10;
		fillLayout.marginHeight = 10;
		shell2.setLayout(fillLayout);

		Button open = new Button(shell2, SWT.PUSH);
		open.setText("Prompt for a String");
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				CronTransUtil1 d = new CronTransUtil1();
				String s = d.open(shell2);
				if (s != null) {
					performTimeText2.setText(s);
				}

			}
		});
		performTimeText2 = new Text(shell2, SWT.BORDER | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL);
		shell2.pack();
		shell2.open();

		while (!shell2.isDisposed()) {// 检验窗体是否关闭
			if (!display.readAndDispatch()) // 检验display线程状态是否忙
				display.sleep();// Display类线程休眠
		}
		display.dispose();// 注销Display对象资源
	}

	/**
	 * 打开窗口.
	 */
	public String open(Shell cronShell) {
		final Shell shell = new Shell(cronShell, SWT.CLOSE | SWT.MIN);
		this.shell = shell;
		// 获取当前显示器分辨率
		Rectangle area = Display.getDefault().getClientArea();
		shell.setLocation(area.width * 1 / 6, area.height * 1 / 10);
		shell.setSize(700, 600);
		shell.setText("Cron表达式");
		createContents();
		shell.open();
		//shell.layout();
		display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return cronExpressionReturn;
	}

	// 初始化容器
	private void createContents() {
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		shell.setLayout(formLayout);

		// 上边框布局
		CTabFolder = new CTabFolder(shell, SWT.BORDER);
		CTabFolder.setFocus();

		FormData CTabFolderFormData = new FormData();
		CTabFolderFormData.top = new FormAttachment(0, 0);
		CTabFolderFormData.bottom = new FormAttachment(40, 0);
		CTabFolderFormData.left = new FormAttachment(0, 0);
		CTabFolderFormData.right = new FormAttachment(100, 0);
		CTabFolder.setLayoutData(CTabFolderFormData);

		// 秒
		CTabItem sCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		sCTabItem.setText("秒");

		Composite sComposite = new Composite(CTabFolder, SWT.NONE);
		sComposite.setLayout(new FormLayout());
		sCTabItem.setControl(sComposite);
		FormData sCompositeFormData = new FormData();
		sCompositeFormData.top = new FormAttachment(0, 0);
		sCompositeFormData.bottom = new FormAttachment(100, 0);
		sCompositeFormData.left = new FormAttachment(0, 0);
		sCompositeFormData.right = new FormAttachment(100, 0);
		sComposite.setLayoutData(sCompositeFormData);

		sTriggerButton = new Button(sComposite, SWT.RADIO);
		sTriggerButton.setText("每秒触发");
		sTriggerButton.setSelection(true);
		sTriggerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = sTriggerButton.getSelection();
				if (selected == true) {
					sComboStart.setEnabled(false);
					sComboEvery.setEnabled(false);
					for (Button cb : s_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormData sTriggerButtonFormData = new FormData();
		sTriggerButtonFormData.top = new FormAttachment(5, 0);
		sTriggerButtonFormData.bottom = new FormAttachment(15, 0);
		sTriggerButtonFormData.left = new FormAttachment(5, 0);
		sTriggerButtonFormData.right = new FormAttachment(100, 0);
		sTriggerButton.setLayoutData(sTriggerButtonFormData);

		sCycleButton = new Button(sComposite, SWT.RADIO);
		sCycleButton.setText("循环 ");
		sCycleButton.setSelection(false);
		sCycleButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = sCycleButton.getSelection();
				if (selected == true) {
					sComboStart.setEnabled(selected);
					sComboEvery.setEnabled(selected);
					for (Button cb : s_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormData sCycleButtonFormData = new FormData();
		sCycleButtonFormData.top = new FormAttachment(16, 0);
		sCycleButtonFormData.bottom = new FormAttachment(26, 0);
		sCycleButtonFormData.left = new FormAttachment(5, 0);
		sCycleButtonFormData.right = new FormAttachment(12, 0);
		sCycleButton.setLayoutData(sCycleButtonFormData);

		CLabel sCLabelCycle1 = new CLabel(sComposite, SWT.NONE);
		sCLabelCycle1.setText("从");
		FormAttachment sCycleButtonFormAttachment = new FormAttachment(
				sCycleButton);
		FormData sCLabelCycle1FormData = new FormData();
		sCLabelCycle1FormData.top = new FormAttachment(16, 0);
		sCLabelCycle1FormData.bottom = new FormAttachment(26, 0);
		sCLabelCycle1FormData.left = sCycleButtonFormAttachment;
		sCLabelCycle1FormData.right = new FormAttachment(15, 0);
		sCLabelCycle1.setLayoutData(sCLabelCycle1FormData);

		sComboStart = new CCombo(sComposite, SWT.READ_ONLY | SWT.FLAT
				| SWT.BORDER);
		for (int i = 0; i < 60; i++) {
			sComboStart.add(i + "");
		}
		sComboStart.setText("0");
		sComboStart.setEnabled(false);

		FormAttachment sCLabelCycle1FormAttachment = new FormAttachment(
				sCLabelCycle1);
		FormData sComboStartFormData = new FormData();
		sComboStartFormData.top = new FormAttachment(16, 0);
		sComboStartFormData.bottom = new FormAttachment(26, 0);
		sComboStartFormData.left = sCLabelCycle1FormAttachment;
		sComboStartFormData.right = new FormAttachment(21, 0);
		sComboStart.setLayoutData(sComboStartFormData);

		CLabel sCLabelCycle2 = new CLabel(sComposite, SWT.NONE);
		sCLabelCycle2.setText("秒开始，每");

		FormAttachment sComboStartFormAttachment = new FormAttachment(
				sComboStart);
		FormData sCLabelCycle2FormData = new FormData();
		sCLabelCycle2FormData.top = new FormAttachment(16, 0);
		sCLabelCycle2FormData.bottom = new FormAttachment(26, 0);
		sCLabelCycle2FormData.left = sComboStartFormAttachment;
		sCLabelCycle2FormData.right = new FormAttachment(31, 0);
		sCLabelCycle2.setLayoutData(sCLabelCycle2FormData);

		sComboEvery = new CCombo(sComposite, SWT.READ_ONLY | SWT.FLAT
				| SWT.BORDER);
		for (int i = 0; i < 30; i++) {
			sComboEvery.add(i + "");
		}
		sComboEvery.setText("5");
		sComboEvery.setEnabled(false);

		FormAttachment sCLabelCycle2FormAttachment = new FormAttachment(
				sCLabelCycle2);
		FormData sComboEveryFormData = new FormData();
		sComboEveryFormData.top = new FormAttachment(16, 0);
		sComboEveryFormData.bottom = new FormAttachment(26, 0);
		sComboEveryFormData.left = sCLabelCycle2FormAttachment;
		sComboEveryFormData.right = new FormAttachment(37, 0);
		sComboEvery.setLayoutData(sComboEveryFormData);

		CLabel sCLabelCycle3 = new CLabel(sComposite, SWT.NONE);
		sCLabelCycle3.setText("秒触发");

		FormAttachment sComboEveryFormAttachment = new FormAttachment(
				sComboEvery);
		FormData sCLabelCycle3FormData = new FormData();
		sCLabelCycle3FormData.top = new FormAttachment(16, 0);
		sCLabelCycle3FormData.bottom = new FormAttachment(26, 0);
		sCLabelCycle3FormData.left = sComboEveryFormAttachment;
		sCLabelCycle3FormData.right = new FormAttachment(44, 0);
		sCLabelCycle3.setLayoutData(sCLabelCycle3FormData);

		sSpecifyButton = new Button(sComposite, SWT.RADIO);
		sSpecifyButton.setText("指定");
		sSpecifyButton.setSelection(false);
		sSpecifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = sSpecifyButton.getSelection();
				if (selected == true) {
					sComboStart.setEnabled(false);
					sComboEvery.setEnabled(false);
					for (Button cb : s_select_map.values()) {
						cb.setEnabled(selected);
					}

				}
			}
		});

		FormData sSpecifyButtonFormData = new FormData();
		sSpecifyButtonFormData.top = new FormAttachment(27, 0);
		sSpecifyButtonFormData.bottom = new FormAttachment(37, 0);
		sSpecifyButtonFormData.left = new FormAttachment(5, 0);
		;
		sSpecifyButtonFormData.right = new FormAttachment(14, 0);
		sSpecifyButton.setLayoutData(sSpecifyButtonFormData);

		Group sSelectGroup = new Group(sComposite, SWT.NONE);
		GridLayout sLayout = new GridLayout();
		sLayout.numColumns = 15;
		sSelectGroup.setLayout(sLayout);

		FormData sSelectGroupFormData = new FormData();
		sSelectGroupFormData.top = new FormAttachment(35, 0);
		sSelectGroupFormData.bottom = new FormAttachment(98, 0);
		sSelectGroupFormData.left = new FormAttachment(7, 0);
		sSelectGroupFormData.right = new FormAttachment(98, 0);
		sSelectGroup.setLayoutData(sSelectGroupFormData);

		s_select_0 = new Button(sSelectGroup, SWT.CHECK);
		s_select_1 = new Button(sSelectGroup, SWT.CHECK);
		s_select_2 = new Button(sSelectGroup, SWT.CHECK);
		s_select_3 = new Button(sSelectGroup, SWT.CHECK);
		s_select_4 = new Button(sSelectGroup, SWT.CHECK);
		s_select_5 = new Button(sSelectGroup, SWT.CHECK);
		s_select_6 = new Button(sSelectGroup, SWT.CHECK);
		s_select_7 = new Button(sSelectGroup, SWT.CHECK);
		s_select_8 = new Button(sSelectGroup, SWT.CHECK);
		s_select_9 = new Button(sSelectGroup, SWT.CHECK);
		s_select_10 = new Button(sSelectGroup, SWT.CHECK);
		s_select_11 = new Button(sSelectGroup, SWT.CHECK);
		s_select_12 = new Button(sSelectGroup, SWT.CHECK);
		s_select_13 = new Button(sSelectGroup, SWT.CHECK);
		s_select_14 = new Button(sSelectGroup, SWT.CHECK);
		s_select_15 = new Button(sSelectGroup, SWT.CHECK);
		s_select_16 = new Button(sSelectGroup, SWT.CHECK);
		s_select_17 = new Button(sSelectGroup, SWT.CHECK);
		s_select_18 = new Button(sSelectGroup, SWT.CHECK);
		s_select_19 = new Button(sSelectGroup, SWT.CHECK);
		s_select_20 = new Button(sSelectGroup, SWT.CHECK);
		s_select_21 = new Button(sSelectGroup, SWT.CHECK);
		s_select_22 = new Button(sSelectGroup, SWT.CHECK);
		s_select_23 = new Button(sSelectGroup, SWT.CHECK);
		s_select_24 = new Button(sSelectGroup, SWT.CHECK);
		s_select_25 = new Button(sSelectGroup, SWT.CHECK);
		s_select_26 = new Button(sSelectGroup, SWT.CHECK);
		s_select_27 = new Button(sSelectGroup, SWT.CHECK);
		s_select_28 = new Button(sSelectGroup, SWT.CHECK);
		s_select_29 = new Button(sSelectGroup, SWT.CHECK);
		s_select_30 = new Button(sSelectGroup, SWT.CHECK);
		s_select_31 = new Button(sSelectGroup, SWT.CHECK);
		s_select_32 = new Button(sSelectGroup, SWT.CHECK);
		s_select_33 = new Button(sSelectGroup, SWT.CHECK);
		s_select_34 = new Button(sSelectGroup, SWT.CHECK);
		s_select_35 = new Button(sSelectGroup, SWT.CHECK);
		s_select_36 = new Button(sSelectGroup, SWT.CHECK);
		s_select_37 = new Button(sSelectGroup, SWT.CHECK);
		s_select_38 = new Button(sSelectGroup, SWT.CHECK);
		s_select_39 = new Button(sSelectGroup, SWT.CHECK);
		s_select_40 = new Button(sSelectGroup, SWT.CHECK);
		s_select_41 = new Button(sSelectGroup, SWT.CHECK);
		s_select_42 = new Button(sSelectGroup, SWT.CHECK);
		s_select_43 = new Button(sSelectGroup, SWT.CHECK);
		s_select_44 = new Button(sSelectGroup, SWT.CHECK);
		s_select_45 = new Button(sSelectGroup, SWT.CHECK);
		s_select_46 = new Button(sSelectGroup, SWT.CHECK);
		s_select_47 = new Button(sSelectGroup, SWT.CHECK);
		s_select_48 = new Button(sSelectGroup, SWT.CHECK);
		s_select_49 = new Button(sSelectGroup, SWT.CHECK);
		s_select_50 = new Button(sSelectGroup, SWT.CHECK);
		s_select_51 = new Button(sSelectGroup, SWT.CHECK);
		s_select_52 = new Button(sSelectGroup, SWT.CHECK);
		s_select_53 = new Button(sSelectGroup, SWT.CHECK);
		s_select_54 = new Button(sSelectGroup, SWT.CHECK);
		s_select_55 = new Button(sSelectGroup, SWT.CHECK);
		s_select_56 = new Button(sSelectGroup, SWT.CHECK);
		s_select_57 = new Button(sSelectGroup, SWT.CHECK);
		s_select_58 = new Button(sSelectGroup, SWT.CHECK);
		s_select_59 = new Button(sSelectGroup, SWT.CHECK);

		s_select_map.put(0, s_select_0);
		s_select_map.put(1, s_select_1);
		s_select_map.put(2, s_select_2);
		s_select_map.put(3, s_select_3);
		s_select_map.put(4, s_select_4);
		s_select_map.put(5, s_select_5);
		s_select_map.put(6, s_select_6);
		s_select_map.put(7, s_select_7);
		s_select_map.put(8, s_select_8);
		s_select_map.put(9, s_select_9);
		s_select_map.put(10, s_select_10);
		s_select_map.put(11, s_select_11);
		s_select_map.put(12, s_select_12);
		s_select_map.put(13, s_select_13);
		s_select_map.put(14, s_select_14);
		s_select_map.put(15, s_select_15);
		s_select_map.put(16, s_select_16);
		s_select_map.put(17, s_select_17);
		s_select_map.put(18, s_select_18);
		s_select_map.put(19, s_select_19);
		s_select_map.put(20, s_select_20);
		s_select_map.put(21, s_select_21);
		s_select_map.put(22, s_select_22);
		s_select_map.put(23, s_select_23);
		s_select_map.put(24, s_select_24);
		s_select_map.put(25, s_select_25);
		s_select_map.put(26, s_select_26);
		s_select_map.put(27, s_select_27);
		s_select_map.put(28, s_select_28);
		s_select_map.put(29, s_select_29);
		s_select_map.put(30, s_select_30);
		s_select_map.put(31, s_select_31);
		s_select_map.put(32, s_select_32);
		s_select_map.put(33, s_select_33);
		s_select_map.put(34, s_select_34);
		s_select_map.put(35, s_select_35);
		s_select_map.put(36, s_select_36);
		s_select_map.put(37, s_select_37);
		s_select_map.put(38, s_select_38);
		s_select_map.put(39, s_select_39);
		s_select_map.put(40, s_select_40);
		s_select_map.put(41, s_select_41);
		s_select_map.put(42, s_select_42);
		s_select_map.put(43, s_select_43);
		s_select_map.put(44, s_select_44);
		s_select_map.put(45, s_select_45);
		s_select_map.put(46, s_select_46);
		s_select_map.put(47, s_select_47);
		s_select_map.put(48, s_select_48);
		s_select_map.put(49, s_select_49);
		s_select_map.put(50, s_select_50);
		s_select_map.put(51, s_select_51);
		s_select_map.put(52, s_select_52);
		s_select_map.put(53, s_select_53);
		s_select_map.put(54, s_select_54);
		s_select_map.put(55, s_select_55);
		s_select_map.put(56, s_select_56);
		s_select_map.put(57, s_select_57);
		s_select_map.put(58, s_select_58);
		s_select_map.put(59, s_select_59);

		for (int key : s_select_map.keySet()) {
			button = s_select_map.get(key);
			button.setText(key + "");
			// button.setGrayed(true);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// 分
		CTabItem minCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		minCTabItem.setText("分");

		Composite minComposite = new Composite(CTabFolder, SWT.NONE);
		minComposite.setLayout(new FormLayout());
		minCTabItem.setControl(minComposite);
		FormData minCompositeFormData = new FormData();
		minCompositeFormData.top = new FormAttachment(0, 0);
		minCompositeFormData.bottom = new FormAttachment(100, 0);
		minCompositeFormData.left = new FormAttachment(0, 0);
		minCompositeFormData.right = new FormAttachment(100, 0);
		minComposite.setLayoutData(minCompositeFormData);

		minTriggerButton = new Button(minComposite, SWT.RADIO);
		minTriggerButton.setText("每分钟触发");
		minTriggerButton.setSelection(true);
		minTriggerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = minTriggerButton.getSelection();
				if (selected == true) {
					minComboStart.setEnabled(false);
					minComboEvery.setEnabled(false);
					for (Button cb : min_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormData minTriggerButtonFormData = new FormData();
		minTriggerButtonFormData.top = new FormAttachment(5, 0);
		minTriggerButtonFormData.bottom = new FormAttachment(15, 0);
		minTriggerButtonFormData.left = new FormAttachment(5, 0);
		minTriggerButtonFormData.right = new FormAttachment(100, 0);
		minTriggerButton.setLayoutData(minTriggerButtonFormData);

		minCycleButton = new Button(minComposite, SWT.RADIO);
		minCycleButton.setText("循环 ");
		minCycleButton.setSelection(false);
		minCycleButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = minCycleButton.getSelection();
				if (selected == true) {
					minComboStart.setEnabled(selected);
					minComboEvery.setEnabled(selected);
					for (Button cb : min_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormAttachment minCycleButtonFormAttachment = new FormAttachment(
				minCycleButton);
		FormData minCycleButtonFormData = new FormData();
		minCycleButtonFormData.top = new FormAttachment(16, 0);
		minCycleButtonFormData.bottom = new FormAttachment(26, 0);
		minCycleButtonFormData.left = new FormAttachment(5, 0);
		minCycleButtonFormData.right = new FormAttachment(12, 0);
		minCycleButton.setLayoutData(minCycleButtonFormData);

		CLabel minCLabelCycle1 = new CLabel(minComposite, SWT.NONE);
		minCLabelCycle1.setText("从");
		FormAttachment minCLabelCycle1FormAttachment = new FormAttachment(
				minCLabelCycle1);

		FormData minCLabelCycle1FormData = new FormData();
		minCLabelCycle1FormData.top = new FormAttachment(16, 0);
		minCLabelCycle1FormData.bottom = new FormAttachment(26, 0);
		minCLabelCycle1FormData.left = minCycleButtonFormAttachment;
		minCLabelCycle1FormData.right = new FormAttachment(15, 0);
		minCLabelCycle1.setLayoutData(minCLabelCycle1FormData);

		minComboStart = new CCombo(minComposite, SWT.READ_ONLY | SWT.FLAT
				| SWT.BORDER);
		for (int i = 0; i < 60; i++) {
			minComboStart.add(i + "");
		}
		minComboStart.setText("0");
		FormAttachment minComboStartFormAttachment = new FormAttachment(
				minComboStart);

		FormData minComboStartFormData = new FormData();
		minComboStartFormData.top = new FormAttachment(16, 0);
		minComboStartFormData.bottom = new FormAttachment(26, 0);
		minComboStartFormData.left = minCLabelCycle1FormAttachment;
		minComboStartFormData.right = new FormAttachment(21, 0);
		minComboStart.setLayoutData(minComboStartFormData);

		CLabel minCLabelCycle2 = new CLabel(minComposite, SWT.NONE);
		minCLabelCycle2.setText("分开始，每");
		FormAttachment minCLabelCycle2FormAttachment = new FormAttachment(
				minCLabelCycle2);

		FormData minCLabelCycle2FormData = new FormData();
		minCLabelCycle2FormData.top = new FormAttachment(16, 0);
		minCLabelCycle2FormData.bottom = new FormAttachment(26, 0);
		minCLabelCycle2FormData.left = minComboStartFormAttachment;
		minCLabelCycle2FormData.right = new FormAttachment(31, 0);
		minCLabelCycle2.setLayoutData(minCLabelCycle2FormData);

		minComboEvery = new CCombo(minComposite, SWT.READ_ONLY | SWT.FLAT
				| SWT.BORDER);
		for (int i = 0; i < 30; i++) {
			minComboEvery.add(i + "");
		}
		minComboEvery.setText("5");

		FormAttachment minComboEveryFormAttachment = new FormAttachment(
				minComboEvery);

		FormData minComboEveryFormData = new FormData();
		minComboEveryFormData.top = new FormAttachment(16, 0);
		minComboEveryFormData.bottom = new FormAttachment(26, 0);
		minComboEveryFormData.left = minCLabelCycle2FormAttachment;
		minComboEveryFormData.right = new FormAttachment(37, 0);
		minComboEvery.setLayoutData(minComboEveryFormData);

		CLabel minCLabelCycle3 = new CLabel(minComposite, SWT.NONE);
		minCLabelCycle3.setText("分触发");

		FormData minCLabelCycle3FormData = new FormData();
		minCLabelCycle3FormData.top = new FormAttachment(16, 0);
		minCLabelCycle3FormData.bottom = new FormAttachment(26, 0);
		minCLabelCycle3FormData.left = minComboEveryFormAttachment;
		minCLabelCycle3FormData.right = new FormAttachment(45, 0);
		minCLabelCycle3.setLayoutData(minCLabelCycle3FormData);

		minSpecifyButton = new Button(minComposite, SWT.RADIO);
		minSpecifyButton.setText("指定");
		minSpecifyButton.setSelection(false);
		minSpecifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = minSpecifyButton.getSelection();
				if (selected == true) {
					minComboStart.setEnabled(false);
					minComboEvery.setEnabled(false);
					for (Button cb : min_select_map.values()) {
						cb.setEnabled(selected);
					}

				}
			}
		});

		FormData minSpecifyButtonFormData = new FormData();
		minSpecifyButtonFormData.top = new FormAttachment(27, 0);
		minSpecifyButtonFormData.bottom = new FormAttachment(37, 0);
		minSpecifyButtonFormData.left = new FormAttachment(5, 0);
		;
		minSpecifyButtonFormData.right = new FormAttachment(14, 0);
		minSpecifyButton.setLayoutData(minSpecifyButtonFormData);

		Group minSelectGroup = new Group(minComposite, SWT.NONE);
		GridLayout minLayout = new GridLayout();
		minLayout.numColumns = 15;
		minSelectGroup.setLayout(minLayout);

		FormData minSelectGroupFormData = new FormData();
		minSelectGroupFormData.top = new FormAttachment(35, 0);
		minSelectGroupFormData.bottom = new FormAttachment(98, 0);
		minSelectGroupFormData.left = new FormAttachment(7, 0);
		minSelectGroupFormData.right = new FormAttachment(98, 0);
		minSelectGroup.setLayoutData(minSelectGroupFormData);

		min_select_0 = new Button(minSelectGroup, SWT.CHECK);
		min_select_1 = new Button(minSelectGroup, SWT.CHECK);
		min_select_2 = new Button(minSelectGroup, SWT.CHECK);
		min_select_3 = new Button(minSelectGroup, SWT.CHECK);
		min_select_4 = new Button(minSelectGroup, SWT.CHECK);
		min_select_5 = new Button(minSelectGroup, SWT.CHECK);
		min_select_6 = new Button(minSelectGroup, SWT.CHECK);
		min_select_7 = new Button(minSelectGroup, SWT.CHECK);
		min_select_8 = new Button(minSelectGroup, SWT.CHECK);
		min_select_9 = new Button(minSelectGroup, SWT.CHECK);
		min_select_10 = new Button(minSelectGroup, SWT.CHECK);
		min_select_11 = new Button(minSelectGroup, SWT.CHECK);
		min_select_12 = new Button(minSelectGroup, SWT.CHECK);
		min_select_13 = new Button(minSelectGroup, SWT.CHECK);
		min_select_14 = new Button(minSelectGroup, SWT.CHECK);
		min_select_15 = new Button(minSelectGroup, SWT.CHECK);
		min_select_16 = new Button(minSelectGroup, SWT.CHECK);
		min_select_17 = new Button(minSelectGroup, SWT.CHECK);
		min_select_18 = new Button(minSelectGroup, SWT.CHECK);
		min_select_19 = new Button(minSelectGroup, SWT.CHECK);
		min_select_20 = new Button(minSelectGroup, SWT.CHECK);
		min_select_21 = new Button(minSelectGroup, SWT.CHECK);
		min_select_22 = new Button(minSelectGroup, SWT.CHECK);
		min_select_23 = new Button(minSelectGroup, SWT.CHECK);
		min_select_24 = new Button(minSelectGroup, SWT.CHECK);
		min_select_25 = new Button(minSelectGroup, SWT.CHECK);
		min_select_26 = new Button(minSelectGroup, SWT.CHECK);
		min_select_27 = new Button(minSelectGroup, SWT.CHECK);
		min_select_28 = new Button(minSelectGroup, SWT.CHECK);
		min_select_29 = new Button(minSelectGroup, SWT.CHECK);
		min_select_30 = new Button(minSelectGroup, SWT.CHECK);
		min_select_31 = new Button(minSelectGroup, SWT.CHECK);
		min_select_32 = new Button(minSelectGroup, SWT.CHECK);
		min_select_33 = new Button(minSelectGroup, SWT.CHECK);
		min_select_34 = new Button(minSelectGroup, SWT.CHECK);
		min_select_35 = new Button(minSelectGroup, SWT.CHECK);
		min_select_36 = new Button(minSelectGroup, SWT.CHECK);
		min_select_37 = new Button(minSelectGroup, SWT.CHECK);
		min_select_38 = new Button(minSelectGroup, SWT.CHECK);
		min_select_39 = new Button(minSelectGroup, SWT.CHECK);
		min_select_40 = new Button(minSelectGroup, SWT.CHECK);
		min_select_41 = new Button(minSelectGroup, SWT.CHECK);
		min_select_42 = new Button(minSelectGroup, SWT.CHECK);
		min_select_43 = new Button(minSelectGroup, SWT.CHECK);
		min_select_44 = new Button(minSelectGroup, SWT.CHECK);
		min_select_45 = new Button(minSelectGroup, SWT.CHECK);
		min_select_46 = new Button(minSelectGroup, SWT.CHECK);
		min_select_47 = new Button(minSelectGroup, SWT.CHECK);
		min_select_48 = new Button(minSelectGroup, SWT.CHECK);
		min_select_49 = new Button(minSelectGroup, SWT.CHECK);
		min_select_50 = new Button(minSelectGroup, SWT.CHECK);
		min_select_51 = new Button(minSelectGroup, SWT.CHECK);
		min_select_52 = new Button(minSelectGroup, SWT.CHECK);
		min_select_53 = new Button(minSelectGroup, SWT.CHECK);
		min_select_54 = new Button(minSelectGroup, SWT.CHECK);
		min_select_55 = new Button(minSelectGroup, SWT.CHECK);
		min_select_56 = new Button(minSelectGroup, SWT.CHECK);
		min_select_57 = new Button(minSelectGroup, SWT.CHECK);
		min_select_58 = new Button(minSelectGroup, SWT.CHECK);
		min_select_59 = new Button(minSelectGroup, SWT.CHECK);

		min_select_map.put(0, min_select_0);
		min_select_map.put(1, min_select_1);
		min_select_map.put(2, min_select_2);
		min_select_map.put(3, min_select_3);
		min_select_map.put(4, min_select_4);
		min_select_map.put(5, min_select_5);
		min_select_map.put(6, min_select_6);
		min_select_map.put(7, min_select_7);
		min_select_map.put(8, min_select_8);
		min_select_map.put(9, min_select_9);
		min_select_map.put(10, min_select_10);
		min_select_map.put(11, min_select_11);
		min_select_map.put(12, min_select_12);
		min_select_map.put(13, min_select_13);
		min_select_map.put(14, min_select_14);
		min_select_map.put(15, min_select_15);
		min_select_map.put(16, min_select_16);
		min_select_map.put(17, min_select_17);
		min_select_map.put(18, min_select_18);
		min_select_map.put(19, min_select_19);
		min_select_map.put(20, min_select_20);
		min_select_map.put(21, min_select_21);
		min_select_map.put(22, min_select_22);
		min_select_map.put(23, min_select_23);
		min_select_map.put(24, min_select_24);
		min_select_map.put(25, min_select_25);
		min_select_map.put(26, min_select_26);
		min_select_map.put(27, min_select_27);
		min_select_map.put(28, min_select_28);
		min_select_map.put(29, min_select_29);
		min_select_map.put(30, min_select_30);
		min_select_map.put(31, min_select_31);
		min_select_map.put(32, min_select_32);
		min_select_map.put(33, min_select_33);
		min_select_map.put(34, min_select_34);
		min_select_map.put(35, min_select_35);
		min_select_map.put(36, min_select_36);
		min_select_map.put(37, min_select_37);
		min_select_map.put(38, min_select_38);
		min_select_map.put(39, min_select_39);
		min_select_map.put(40, min_select_40);
		min_select_map.put(41, min_select_41);
		min_select_map.put(42, min_select_42);
		min_select_map.put(43, min_select_43);
		min_select_map.put(44, min_select_44);
		min_select_map.put(45, min_select_45);
		min_select_map.put(46, min_select_46);
		min_select_map.put(47, min_select_47);
		min_select_map.put(48, min_select_48);
		min_select_map.put(49, min_select_49);
		min_select_map.put(50, min_select_50);
		min_select_map.put(51, min_select_51);
		min_select_map.put(52, min_select_52);
		min_select_map.put(53, min_select_53);
		min_select_map.put(54, min_select_54);
		min_select_map.put(55, min_select_55);
		min_select_map.put(56, min_select_56);
		min_select_map.put(57, min_select_57);
		min_select_map.put(58, min_select_58);
		min_select_map.put(59, min_select_59);

		for (int key : min_select_map.keySet()) {
			button = min_select_map.get(key);
			button.setText(key + "");
			// button.setGrayed(true);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// 小时
		CTabItem hCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		hCTabItem.setText("小时");

		Composite hComposite = new Composite(CTabFolder, SWT.NONE);
		hComposite.setLayout(new FormLayout());
		hCTabItem.setControl(hComposite);
		FormData hCompositeFormData = new FormData();
		hCompositeFormData.top = new FormAttachment(0, 0);
		hCompositeFormData.bottom = new FormAttachment(100, 0);
		hCompositeFormData.left = new FormAttachment(0, 0);
		hCompositeFormData.right = new FormAttachment(100, 0);
		hComposite.setLayoutData(hCompositeFormData);

		hTriggerButton = new Button(hComposite, SWT.RADIO);
		hTriggerButton.setText("每小时触发");
		hTriggerButton.setSelection(true);
		hTriggerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = hTriggerButton.getSelection();
				if (selected == true) {
					for (Button cb : h_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormData hTriggerButtonFormData = new FormData();
		hTriggerButtonFormData.top = new FormAttachment(5, 0);
		hTriggerButtonFormData.bottom = new FormAttachment(15, 0);
		hTriggerButtonFormData.left = new FormAttachment(5, 0);
		hTriggerButtonFormData.right = new FormAttachment(100, 0);
		hTriggerButton.setLayoutData(hTriggerButtonFormData);

		hSpecifyButton = new Button(hComposite, SWT.RADIO);
		hSpecifyButton.setText("指定");
		hSpecifyButton.setSelection(false);
		hSpecifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = hSpecifyButton.getSelection();
				if (selected == true) {
					hTriggerButton.setSelection(false);
					for (Button cb : h_select_map.values()) {
						cb.setEnabled(selected);
					}

				}
			}
		});

		FormData hSpecifyButtonFormData = new FormData();
		hSpecifyButtonFormData.top = new FormAttachment(16, 0);
		hSpecifyButtonFormData.bottom = new FormAttachment(30, 0);
		hSpecifyButtonFormData.left = new FormAttachment(5, 0);
		;
		hSpecifyButtonFormData.right = new FormAttachment(14, 0);
		hSpecifyButton.setLayoutData(hSpecifyButtonFormData);

		Group hSelectGroup = new Group(hComposite, SWT.NONE);
		GridLayout hLayout = new GridLayout();
		hLayout.numColumns = 15;
		hSelectGroup.setLayout(hLayout);

		FormData hSelectGroupFormData = new FormData();
		hSelectGroupFormData.top = new FormAttachment(32, 0);
		hSelectGroupFormData.bottom = new FormAttachment(70, 0);
		hSelectGroupFormData.left = new FormAttachment(7, 0);
		hSelectGroupFormData.right = new FormAttachment(98, 0);
		hSelectGroup.setLayoutData(hSelectGroupFormData);

		h_select_0 = new Button(hSelectGroup, SWT.CHECK);
		h_select_1 = new Button(hSelectGroup, SWT.CHECK);
		h_select_2 = new Button(hSelectGroup, SWT.CHECK);
		h_select_3 = new Button(hSelectGroup, SWT.CHECK);
		h_select_4 = new Button(hSelectGroup, SWT.CHECK);
		h_select_5 = new Button(hSelectGroup, SWT.CHECK);
		h_select_6 = new Button(hSelectGroup, SWT.CHECK);
		h_select_7 = new Button(hSelectGroup, SWT.CHECK);
		h_select_8 = new Button(hSelectGroup, SWT.CHECK);
		h_select_9 = new Button(hSelectGroup, SWT.CHECK);
		h_select_10 = new Button(hSelectGroup, SWT.CHECK);
		h_select_11 = new Button(hSelectGroup, SWT.CHECK);
		h_select_12 = new Button(hSelectGroup, SWT.CHECK);
		h_select_13 = new Button(hSelectGroup, SWT.CHECK);
		h_select_14 = new Button(hSelectGroup, SWT.CHECK);
		h_select_15 = new Button(hSelectGroup, SWT.CHECK);
		h_select_16 = new Button(hSelectGroup, SWT.CHECK);
		h_select_17 = new Button(hSelectGroup, SWT.CHECK);
		h_select_18 = new Button(hSelectGroup, SWT.CHECK);
		h_select_19 = new Button(hSelectGroup, SWT.CHECK);
		h_select_20 = new Button(hSelectGroup, SWT.CHECK);
		h_select_21 = new Button(hSelectGroup, SWT.CHECK);
		h_select_22 = new Button(hSelectGroup, SWT.CHECK);
		h_select_23 = new Button(hSelectGroup, SWT.CHECK);

		h_select_map.put(0, h_select_0);
		h_select_map.put(1, h_select_1);
		h_select_map.put(2, h_select_2);
		h_select_map.put(3, h_select_3);
		h_select_map.put(4, h_select_4);
		h_select_map.put(5, h_select_5);
		h_select_map.put(6, h_select_6);
		h_select_map.put(7, h_select_7);
		h_select_map.put(8, h_select_8);
		h_select_map.put(9, h_select_9);
		h_select_map.put(10, h_select_10);
		h_select_map.put(11, h_select_11);
		h_select_map.put(12, h_select_12);
		h_select_map.put(13, h_select_13);
		h_select_map.put(14, h_select_14);
		h_select_map.put(15, h_select_15);
		h_select_map.put(16, h_select_16);
		h_select_map.put(17, h_select_17);
		h_select_map.put(18, h_select_18);
		h_select_map.put(19, h_select_19);
		h_select_map.put(20, h_select_20);
		h_select_map.put(21, h_select_21);
		h_select_map.put(22, h_select_22);
		h_select_map.put(23, h_select_23);
		new CLabel(hSelectGroup, SWT.NONE);
		new CLabel(hSelectGroup, SWT.NONE);
		new CLabel(hSelectGroup, SWT.NONE);
		new CLabel(hSelectGroup, SWT.NONE);
		new CLabel(hSelectGroup, SWT.NONE);
		new CLabel(hSelectGroup, SWT.NONE);

		for (int key : h_select_map.keySet()) {
			button = h_select_map.get(key);
			button.setText(key + "");
			// button.setGrayed(true);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// 日开始
		CTabItem dCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		dCTabItem.setText("日");

		Composite dComposite = new Composite(CTabFolder, SWT.NONE);
		dComposite.setLayout(new FormLayout());
		dCTabItem.setControl(dComposite);
		FormData dCompositeFormData = new FormData();
		dCompositeFormData.top = new FormAttachment(0, 0);
		dCompositeFormData.bottom = new FormAttachment(100, 0);
		dCompositeFormData.left = new FormAttachment(0, 0);
		dCompositeFormData.right = new FormAttachment(100, 0);
		dComposite.setLayoutData(dCompositeFormData);

		dTriggerButton = new Button(dComposite, SWT.RADIO);
		dTriggerButton.setText("每天触发");
		dTriggerButton.setSelection(true);
		dTriggerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = dTriggerButton.getSelection();
				if (selected == true) {
					for (Button cb : d_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}
			}
		});

		FormData dTriggerButtonFormData = new FormData();
		dTriggerButtonFormData.top = new FormAttachment(5, 0);
		dTriggerButtonFormData.bottom = new FormAttachment(15, 0);
		dTriggerButtonFormData.left = new FormAttachment(5, 0);
		dTriggerButtonFormData.right = new FormAttachment(100, 0);
		dTriggerButton.setLayoutData(dTriggerButtonFormData);

		dSpecifyButton = new Button(dComposite, SWT.RADIO);
		dSpecifyButton.setText("指定");
		dSpecifyButton.setSelection(false);
		dSpecifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = dSpecifyButton.getSelection();
				if (selected == true) {
					dTriggerButton.setSelection(false);
					for (Button cb : d_select_map.values()) {
						cb.setEnabled(selected);
					}

				}
			}
		});

		FormData dSpecifyButtonFormData = new FormData();
		dSpecifyButtonFormData.top = new FormAttachment(16, 0);
		dSpecifyButtonFormData.bottom = new FormAttachment(30, 0);
		dSpecifyButtonFormData.left = new FormAttachment(5, 0);
		dSpecifyButtonFormData.right = new FormAttachment(14, 0);
		dSpecifyButton.setLayoutData(dSpecifyButtonFormData);

		Group dSelectGroup = new Group(dComposite, SWT.NONE);
		GridLayout dLayout = new GridLayout();
		dLayout.numColumns = 15;
		dSelectGroup.setLayout(dLayout);

		FormData dSelectGroupFormData = new FormData();
		dSelectGroupFormData.top = new FormAttachment(32, 0);
		dSelectGroupFormData.bottom = new FormAttachment(80, 0);
		dSelectGroupFormData.left = new FormAttachment(7, 0);
		dSelectGroupFormData.right = new FormAttachment(98, 0);
		dSelectGroup.setLayoutData(dSelectGroupFormData);

		d_select_1 = new Button(dSelectGroup, SWT.CHECK);
		d_select_2 = new Button(dSelectGroup, SWT.CHECK);
		d_select_3 = new Button(dSelectGroup, SWT.CHECK);
		d_select_4 = new Button(dSelectGroup, SWT.CHECK);
		d_select_5 = new Button(dSelectGroup, SWT.CHECK);
		d_select_6 = new Button(dSelectGroup, SWT.CHECK);
		d_select_7 = new Button(dSelectGroup, SWT.CHECK);
		d_select_8 = new Button(dSelectGroup, SWT.CHECK);
		d_select_9 = new Button(dSelectGroup, SWT.CHECK);
		d_select_10 = new Button(dSelectGroup, SWT.CHECK);
		d_select_11 = new Button(dSelectGroup, SWT.CHECK);
		d_select_12 = new Button(dSelectGroup, SWT.CHECK);
		d_select_13 = new Button(dSelectGroup, SWT.CHECK);
		d_select_14 = new Button(dSelectGroup, SWT.CHECK);
		d_select_15 = new Button(dSelectGroup, SWT.CHECK);
		d_select_16 = new Button(dSelectGroup, SWT.CHECK);
		d_select_17 = new Button(dSelectGroup, SWT.CHECK);
		d_select_18 = new Button(dSelectGroup, SWT.CHECK);
		d_select_19 = new Button(dSelectGroup, SWT.CHECK);
		d_select_20 = new Button(dSelectGroup, SWT.CHECK);
		d_select_21 = new Button(dSelectGroup, SWT.CHECK);
		d_select_22 = new Button(dSelectGroup, SWT.CHECK);
		d_select_23 = new Button(dSelectGroup, SWT.CHECK);
		d_select_24 = new Button(dSelectGroup, SWT.CHECK);
		d_select_25 = new Button(dSelectGroup, SWT.CHECK);
		d_select_26 = new Button(dSelectGroup, SWT.CHECK);
		d_select_27 = new Button(dSelectGroup, SWT.CHECK);
		d_select_28 = new Button(dSelectGroup, SWT.CHECK);
		d_select_29 = new Button(dSelectGroup, SWT.CHECK);
		d_select_30 = new Button(dSelectGroup, SWT.CHECK);
		d_select_31 = new Button(dSelectGroup, SWT.CHECK);

		d_select_map.put(1, d_select_1);
		d_select_map.put(2, d_select_2);
		d_select_map.put(3, d_select_3);
		d_select_map.put(4, d_select_4);
		d_select_map.put(5, d_select_5);
		d_select_map.put(6, d_select_6);
		d_select_map.put(7, d_select_7);
		d_select_map.put(8, d_select_8);
		d_select_map.put(9, d_select_9);
		d_select_map.put(10, d_select_10);
		d_select_map.put(11, d_select_11);
		d_select_map.put(12, d_select_12);
		d_select_map.put(13, d_select_13);
		d_select_map.put(14, d_select_14);
		d_select_map.put(15, d_select_15);
		d_select_map.put(16, d_select_16);
		d_select_map.put(17, d_select_17);
		d_select_map.put(18, d_select_18);
		d_select_map.put(19, d_select_19);
		d_select_map.put(20, d_select_20);
		d_select_map.put(21, d_select_21);
		d_select_map.put(22, d_select_22);
		d_select_map.put(23, d_select_23);
		d_select_map.put(24, d_select_24);
		d_select_map.put(25, d_select_25);
		d_select_map.put(26, d_select_26);
		d_select_map.put(27, d_select_27);
		d_select_map.put(28, d_select_28);
		d_select_map.put(29, d_select_29);
		d_select_map.put(30, d_select_30);
		d_select_map.put(31, d_select_31);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);
		new CLabel(dSelectGroup, SWT.NONE);

		for (int key : d_select_map.keySet()) {
			button = d_select_map.get(key);
			button.setText(key + "");
			// button.setGrayed(true);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// 月开始
		CTabItem mCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		mCTabItem.setText("月");

		Composite mComposite = new Composite(CTabFolder, SWT.NONE);
		mComposite.setLayout(new FormLayout());
		mCTabItem.setControl(mComposite);
		FormData mCompositeFormData = new FormData();
		mCompositeFormData.top = new FormAttachment(0, 0);
		mCompositeFormData.bottom = new FormAttachment(100, 0);
		mCompositeFormData.left = new FormAttachment(0, 0);
		mCompositeFormData.right = new FormAttachment(100, 0);
		mComposite.setLayoutData(mCompositeFormData);

		mTriggerButton = new Button(mComposite, SWT.RADIO);
		mTriggerButton.setText("每月触发");
		mTriggerButton.setSelection(true);
		mTriggerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = mTriggerButton.getSelection();
				if (selected == true) {
					for (Button cb : m_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormData mTriggerButtonFormData = new FormData();
		mTriggerButtonFormData.top = new FormAttachment(5, 0);
		mTriggerButtonFormData.bottom = new FormAttachment(15, 0);
		mTriggerButtonFormData.left = new FormAttachment(5, 0);
		mTriggerButtonFormData.right = new FormAttachment(100, 0);
		mTriggerButton.setLayoutData(mTriggerButtonFormData);

		mSpecifyButton = new Button(mComposite, SWT.RADIO);
		mSpecifyButton.setText("指定");
		mSpecifyButton.setSelection(false);
		mSpecifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = mSpecifyButton.getSelection();
				if (selected == true) {
					mTriggerButton.setSelection(false);
					for (Button cb : m_select_map.values()) {
						cb.setEnabled(selected);
					}

				}
			}
		});

		FormData mSpecifyButtonFormData = new FormData();
		mSpecifyButtonFormData.top = new FormAttachment(16, 0);
		mSpecifyButtonFormData.bottom = new FormAttachment(30, 0);
		mSpecifyButtonFormData.left = new FormAttachment(5, 0);
		mSpecifyButtonFormData.right = new FormAttachment(14, 0);
		mSpecifyButton.setLayoutData(mSpecifyButtonFormData);

		Group mSelectGroup = new Group(mComposite, SWT.NONE);
		GridLayout mLayout = new GridLayout();
		mLayout.numColumns = 15;
		mSelectGroup.setLayout(mLayout);

		FormData mSelectGroupFormData = new FormData();
		mSelectGroupFormData.top = new FormAttachment(32, 0);
		mSelectGroupFormData.bottom = new FormAttachment(60, 0);
		mSelectGroupFormData.left = new FormAttachment(7, 0);
		mSelectGroupFormData.right = new FormAttachment(70, 0);
		mSelectGroup.setLayoutData(mSelectGroupFormData);

		m_select_1 = new Button(mSelectGroup, SWT.CHECK);
		m_select_2 = new Button(mSelectGroup, SWT.CHECK);
		m_select_3 = new Button(mSelectGroup, SWT.CHECK);
		m_select_4 = new Button(mSelectGroup, SWT.CHECK);
		m_select_5 = new Button(mSelectGroup, SWT.CHECK);
		m_select_6 = new Button(mSelectGroup, SWT.CHECK);
		m_select_7 = new Button(mSelectGroup, SWT.CHECK);
		m_select_8 = new Button(mSelectGroup, SWT.CHECK);
		m_select_9 = new Button(mSelectGroup, SWT.CHECK);
		m_select_10 = new Button(mSelectGroup, SWT.CHECK);
		m_select_11 = new Button(mSelectGroup, SWT.CHECK);
		m_select_12 = new Button(mSelectGroup, SWT.CHECK);

		m_select_map.put(1, m_select_1);
		m_select_map.put(2, m_select_2);
		m_select_map.put(3, m_select_3);
		m_select_map.put(4, m_select_4);
		m_select_map.put(5, m_select_5);
		m_select_map.put(6, m_select_6);
		m_select_map.put(7, m_select_7);
		m_select_map.put(8, m_select_8);
		m_select_map.put(9, m_select_9);
		m_select_map.put(10, m_select_10);
		m_select_map.put(11, m_select_11);
		m_select_map.put(12, m_select_12);

		for (int key : m_select_map.keySet()) {
			button = m_select_map.get(key);
			button.setText(key + "");
			// button.setGrayed(true);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// 周开始
		CTabItem wCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		wCTabItem.setText("周");

		Composite wComposite = new Composite(CTabFolder, SWT.NONE);
		wComposite.setLayout(new FormLayout());
		wCTabItem.setControl(wComposite);
		FormData wCompositeFormData = new FormData();
		wCompositeFormData.top = new FormAttachment(0, 0);
		wCompositeFormData.bottom = new FormAttachment(100, 0);
		wCompositeFormData.left = new FormAttachment(0, 0);
		wCompositeFormData.right = new FormAttachment(100, 0);
		wComposite.setLayoutData(wCompositeFormData);

		wConfigurationButton = new Button(wComposite, SWT.CHECK);
		wConfigurationButton.setText("使用周配置");
		wConfigurationButton.setSelection(false);
		wConfigurationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = wConfigurationButton.getSelection();
				if (selected == true) {
					wConfigurationButton.setSelection(true);
					wTriggerButton.setEnabled(true);
					wSpecifyButton.setEnabled(true);
					wSpecifyButton.setSelection(false);
					for (Button cb : w_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}

			}
		});

		FormData wConfigurationButtonFormData = new FormData();
		wConfigurationButtonFormData.top = new FormAttachment(5, 0);
		wConfigurationButtonFormData.bottom = new FormAttachment(15, 0);
		wConfigurationButtonFormData.left = new FormAttachment(5, 0);
		wConfigurationButtonFormData.right = new FormAttachment(100, 0);
		wConfigurationButton.setLayoutData(wConfigurationButtonFormData);

		Group wBigSelectGroup = new Group(wComposite, SWT.NONE);
		wBigSelectGroup.setLayout(new FormLayout());

		FormData wSelectBigGroupFormData = new FormData();
		wSelectBigGroupFormData.top = new FormAttachment(18, 0);
		wSelectBigGroupFormData.bottom = new FormAttachment(80, 0);
		wSelectBigGroupFormData.left = new FormAttachment(10, 0);
		wSelectBigGroupFormData.right = new FormAttachment(80, 0);
		wBigSelectGroup.setLayoutData(wSelectBigGroupFormData);

		wTriggerButton = new Button(wBigSelectGroup, SWT.CHECK);
		wTriggerButton.setText("每周触发");
		wTriggerButton.setSelection(false);
		wTriggerButton.setEnabled(false);
		wTriggerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = wTriggerButton.getSelection();
				if (selected == true) {
					wSpecifyButton.setSelection(false);
					for (Button cb : w_select_map.values()) {
						cb.setEnabled(!selected);
					}

				}
			}
		});

		FormData wTriggerButtonFormData = new FormData();
		wTriggerButtonFormData.top = new FormAttachment(5, 0);
		wTriggerButtonFormData.bottom = new FormAttachment(20, 0);
		wTriggerButtonFormData.left = new FormAttachment(2, 0);
		wTriggerButtonFormData.right = new FormAttachment(70, 0);
		wTriggerButton.setLayoutData(wTriggerButtonFormData);

		wSpecifyButton = new Button(wBigSelectGroup, SWT.CHECK);
		wSpecifyButton.setText("指定");
		wSpecifyButton.setSelection(false);
		wSpecifyButton.setEnabled(false);
		wSpecifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = wSpecifyButton.getSelection();
				if (selected == true) {
					wTriggerButton.setSelection(false);
					for (Button cb : w_select_map.values()) {
						cb.setEnabled(selected);
					}

				}
			}
		});

		FormData wSpecifyButtonFormData = new FormData();
		wSpecifyButtonFormData.top = new FormAttachment(25, 0);
		wSpecifyButtonFormData.bottom = new FormAttachment(40, 0);
		wSpecifyButtonFormData.left = new FormAttachment(2, 0);
		wSpecifyButtonFormData.right = new FormAttachment(70, 0);
		wSpecifyButton.setLayoutData(wSpecifyButtonFormData);

		Group wSelectGroup = new Group(wBigSelectGroup, SWT.NONE);
		GridLayout wLayout = new GridLayout();
		wLayout.numColumns = 8;
		wSelectGroup.setLayout(wLayout);

		FormData wSelectGroupFormData = new FormData();
		wSelectGroupFormData.top = new FormAttachment(35, 0);
		wSelectGroupFormData.bottom = new FormAttachment(80, 0);
		wSelectGroupFormData.left = new FormAttachment(3, 0);
		wSelectGroupFormData.right = new FormAttachment(80, 0);
		wSelectGroup.setLayoutData(wSelectGroupFormData);

		w_select_0 = new Button(wSelectGroup, SWT.CHECK);
		w_select_1 = new Button(wSelectGroup, SWT.CHECK);
		w_select_2 = new Button(wSelectGroup, SWT.CHECK);
		w_select_3 = new Button(wSelectGroup, SWT.CHECK);
		w_select_4 = new Button(wSelectGroup, SWT.CHECK);
		w_select_5 = new Button(wSelectGroup, SWT.CHECK);
		w_select_6 = new Button(wSelectGroup, SWT.CHECK);

		w_select_0.setText("周日");
		w_select_1.setText("周一");
		w_select_2.setText("周二");
		w_select_3.setText("周三");
		w_select_4.setText("周四");
		w_select_5.setText("周五");
		w_select_6.setText("周六");

		w_select_map.put(1, w_select_0);
		w_select_map.put(2, w_select_1);
		w_select_map.put(3, w_select_2);
		w_select_map.put(4, w_select_3);
		w_select_map.put(5, w_select_4);
		w_select_map.put(6, w_select_5);
		w_select_map.put(7, w_select_6);
		new CLabel(wSelectGroup, SWT.NONE);

		for (int key : w_select_map.keySet()) {
			button = w_select_map.get(key);
			// button.setGrayed(true);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// 中间按钮
		Composite centerComposite = new Composite(shell, SWT.NONE);
		centerComposite.setLayout(new FormLayout());
		FormData centerCompositeFormData = new FormData();
		centerCompositeFormData.top = new FormAttachment(40, 0);
		centerCompositeFormData.bottom = new FormAttachment(45, 0);
		centerCompositeFormData.left = new FormAttachment(0, 0);
		centerCompositeFormData.right = new FormAttachment(100, 0);
		centerComposite.setLayoutData(centerCompositeFormData);

		Button centerButton = new Button(centerComposite, SWT.NONE);
		centerButton.setText("生成表达式");
		FormData centerButtonFormData = new FormData();
		centerButtonFormData.top = new FormAttachment(5, 0);
		centerButtonFormData.bottom = new FormAttachment(95, 0);
		centerButtonFormData.left = new FormAttachment(5, 0);
		centerButtonFormData.right = new FormAttachment(15, 0);
		centerButton.setLayoutData(centerButtonFormData);

		centerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateCronExpression(e);
			}
		});

		// 3下边框布局
		Group downGroup = new Group(shell, SWT.NONE);
		downGroup.setText("表达式");
		downGroup.setLayout(new FormLayout());

		FormData expressionGroupFormData = new FormData();
		expressionGroupFormData.top = new FormAttachment(45, 0);
		expressionGroupFormData.bottom = new FormAttachment(94, 0);
		expressionGroupFormData.left = new FormAttachment(0, 0);
		expressionGroupFormData.right = new FormAttachment(100, 0);
		downGroup.setLayoutData(expressionGroupFormData);

		CLabel expressionCLabel = new CLabel(downGroup, SWT.NONE);
		expressionCLabel.setText("Cron表达式:");
		FormData bexpressionCLabelFormData = new FormData();
		bexpressionCLabelFormData.top = new FormAttachment(2, 0);
		bexpressionCLabelFormData.bottom = new FormAttachment(10, 0);
		bexpressionCLabelFormData.left = new FormAttachment(3, 0);
		bexpressionCLabelFormData.right = new FormAttachment(14, 0);
		expressionCLabel.setLayoutData(bexpressionCLabelFormData);

		expressionText = new Text(downGroup, SWT.BORDER);
		FormData expressionTextFormData = new FormData();

		expressionTextFormData.top = new FormAttachment(2, 0);
		expressionTextFormData.bottom = new FormAttachment(10, 0);
		expressionTextFormData.left = new FormAttachment(16, 0);
		expressionTextFormData.right = new FormAttachment(50, 0);
		expressionText.setLayoutData(expressionTextFormData);

		Button buttonResolutionToView = new Button(downGroup, SWT.NONE);
		buttonResolutionToView.setText("解析到界面");
		FormData buttonResolutionToViewFormData = new FormData();
		buttonResolutionToViewFormData.top = new FormAttachment(2, 0);
		buttonResolutionToViewFormData.bottom = new FormAttachment(10, 0);
		buttonResolutionToViewFormData.left = new FormAttachment(52, 0);
		buttonResolutionToViewFormData.right = new FormAttachment(64, 0);
		buttonResolutionToView.setLayoutData(buttonResolutionToViewFormData);

		buttonResolutionToView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parseToUI(e);
			}
		});

		Group executionTimePlanGroup = new Group(downGroup, SWT.NONE);
		executionTimePlanGroup.setText("执行时间计划");
		executionTimePlanGroup.setLayout(new FormLayout());

		FormData executionTimePlanGroupFormData = new FormData();
		executionTimePlanGroupFormData.top = new FormAttachment(12, 0);
		executionTimePlanGroupFormData.bottom = new FormAttachment(100, 0);
		executionTimePlanGroupFormData.left = new FormAttachment(2, 0);
		executionTimePlanGroupFormData.right = new FormAttachment(100, 0);
		executionTimePlanGroup.setLayoutData(executionTimePlanGroupFormData);

		Composite executionTimePlanGroupComposite = new Composite(
				executionTimePlanGroup, SWT.BORDER);
		executionTimePlanGroupComposite.setLayout(new FormLayout());

		FormData executionTimePlanGroupCompositeFormData = new FormData();
		executionTimePlanGroupCompositeFormData.top = new FormAttachment(0, 0);
		executionTimePlanGroupCompositeFormData.bottom = new FormAttachment(
				100, 0);
		executionTimePlanGroupCompositeFormData.left = new FormAttachment(0, 0);
		executionTimePlanGroupCompositeFormData.right = new FormAttachment(100,
				0);
		executionTimePlanGroupComposite
				.setLayoutData(executionTimePlanGroupCompositeFormData);

		CLabel startTimeCLabel = new CLabel(executionTimePlanGroupComposite,
				SWT.NONE);
		startTimeCLabel.setText("开始时间:");

		FormData startTimeCLabelFormData = new FormData();
		startTimeCLabelFormData.top = new FormAttachment(5, 0);
		startTimeCLabelFormData.bottom = new FormAttachment(20, 0);
		startTimeCLabelFormData.left = new FormAttachment(5, 0);
		startTimeCLabelFormData.right = new FormAttachment(15, 0);
		startTimeCLabel.setLayoutData(startTimeCLabelFormData);

		startTimeText = new Text(executionTimePlanGroupComposite, SWT.BORDER);
		FormData startTimeTextFormData = new FormData();
		startTimeTextFormData.top = new FormAttachment(5, 0);
		startTimeTextFormData.bottom = new FormAttachment(15, 0);
		startTimeTextFormData.left = new FormAttachment(15, 0);
		startTimeTextFormData.right = new FormAttachment(80, 0);
		startTimeText.setLayoutData(startTimeTextFormData);

		CLabel performTimeCLabel = new CLabel(executionTimePlanGroupComposite,
				SWT.NONE);
		performTimeCLabel.setText("执行时间:");

		FormData performTimeCLabelFormData = new FormData();
		performTimeCLabelFormData.top = new FormAttachment(20, 0);
		performTimeCLabelFormData.bottom = new FormAttachment(30, 0);
		performTimeCLabelFormData.left = new FormAttachment(5, 0);
		performTimeCLabelFormData.right = new FormAttachment(15, 0);
		performTimeCLabel.setLayoutData(performTimeCLabelFormData);

		performTimeText = new Text(executionTimePlanGroupComposite, SWT.BORDER
				| SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData performTimeTextFormData = new FormData();
		performTimeTextFormData.top = new FormAttachment(22, 0);
		performTimeTextFormData.bottom = new FormAttachment(90, 0);
		performTimeTextFormData.left = new FormAttachment(15, 0);
		performTimeTextFormData.right = new FormAttachment(80, 0);
		performTimeText.setLayoutData(performTimeTextFormData);

		// 4 确认取消按钮
		Composite bottomComposite = new Composite(shell, SWT.NONE);
		bottomComposite.setLayout(new FormLayout());
		FormData bottomCompositeFormData = new FormData();
		bottomCompositeFormData.top = new FormAttachment(94, 0);
		bottomCompositeFormData.bottom = new FormAttachment(100, 0);
		bottomCompositeFormData.left = new FormAttachment(0, 0);
		bottomCompositeFormData.right = new FormAttachment(100, 0);
		bottomComposite.setLayoutData(bottomCompositeFormData);

		Button confirmButton = new Button(bottomComposite, SWT.NONE);
		confirmButton.setText("确认");
		FormData confirmButtonFormData = new FormData();
		confirmButtonFormData.top = new FormAttachment(20, 0);
		confirmButtonFormData.bottom = new FormAttachment(80, 0);
		confirmButtonFormData.left = new FormAttachment(30, 0);
		confirmButtonFormData.right = new FormAttachment(40, 0);
		confirmButton.setLayoutData(confirmButtonFormData);

		confirmButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cronExpressionReturn = expressionText.getText().trim();
				shell.close();
			}
		});

		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION
						| SWT.YES | SWT.NO);
				messagebox.setText("提示");
				messagebox.setMessage("您确定要退出吗?");
				int message = messagebox.open();
				e.doit = message == SWT.YES;
			}
		});

		Button cancelButton = new Button(bottomComposite, SWT.NONE);
		cancelButton.setText("取消");
		FormData cancelButtonFormData = new FormData();
		cancelButtonFormData.top = new FormAttachment(20, 0);
		cancelButtonFormData.bottom = new FormAttachment(80, 0);
		cancelButtonFormData.left = new FormAttachment(60, 0);
		cancelButtonFormData.right = new FormAttachment(70, 0);
		cancelButton.setLayoutData(cancelButtonFormData);

		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}

	public void postInitUI() {
	}

	/**
	 * 生成Cron表达式
	 * 
	 * @param evt
	 */
	private void generateCronExpression(SelectionEvent e) {
		// 秒钟数据
		try {
			if (sTriggerButton.getSelection()) {
				dateData.put("s", "*");
			} else if (sCycleButton.getSelection()) {
				dateData.put("s", String.valueOf(sComboStart.getText()) + "/"
						+ String.valueOf(sComboEvery.getText()));
			} else {
				StringBuilder buf = new StringBuilder();
				boolean first = true;
				// 遍历集合把选中的分钟放入StringBuilder
				for (int key : s_select_map.keySet()) {
					Button cb = s_select_map.get(key);
					if (cb.getSelection()) {
						if (!first) {
							buf.append(",");
						}
						buf.append(key);
						first = false;
					}
				}
				dateData.put("s", buf.toString());
				if (buf.length() == 0) {
					MessageDialog.openError(shell, "错误", "没有指定秒！");
					return;
				}

			}

			if (minTriggerButton.getSelection()) {
				dateData.put("min", String.valueOf("*"));
			} else if (minCycleButton.getSelection()) {
				dateData.put("min", String.valueOf(minComboStart.getText())
						+ "/" + String.valueOf(minComboEvery.getText()));
			} else {
				StringBuilder buf = new StringBuilder();
				boolean first = true;
				// 遍历集合把选中的分钟放入StringBuilder
				for (int key : min_select_map.keySet()) {
					Button cb = min_select_map.get(key);
					if (cb.getSelection()) {
						if (!first) {
							buf.append(",");
						}
						buf.append(key);
						first = false;
					}
				}
				dateData.put("min", buf.toString());

				if (buf.length() == 0) {
					MessageDialog.openError(shell, "错误", "没有指定分！");
					return;
				}
			}

			if (hTriggerButton.getSelection()) {
				dateData.put("h", String.valueOf("*"));
			} else {
				StringBuilder buf = new StringBuilder();
				boolean first = true;
				// 遍历集合把选中的分钟放入StringBuilder
				for (int key : h_select_map.keySet()) {
					Button cb = h_select_map.get(key);
					if (cb.getSelection()) {
						if (!first) {
							buf.append(",");
						}
						buf.append(key);
						first = false;
					}
				}
				dateData.put("h", buf.toString());
				if (buf.length() == 0) {
					MessageDialog.openError(shell, "错误", "没有指定时！");
					return;
				}
			}

			if (wConfigurationButton.getSelection()) {// 周，周和天只能选择一个
				dateData.put("d", String.valueOf("?"));
				if (wTriggerButton.getSelection()) {
					dateData.put("w", String.valueOf("*"));
				} else {
					StringBuilder buf = new StringBuilder();
					boolean first = true;
					for (int key : w_select_map.keySet()) {
						Button cb = w_select_map.get(key);
						if (cb.getSelection()) {
							if (!first) {
								buf.append(",");
							}
							buf.append(key);
							first = false;
						}
					}
					dateData.put("w", buf.toString());
					if (buf.length() == 0) {
						MessageDialog.openError(shell, "错误", "没有指定周！");
						return;
					}

				}
			} else {
				dateData.put("w", "?");
				if (dTriggerButton.getSelection()) {// 天
					dateData.put("d", String.valueOf("*"));
				} else {
					StringBuilder buf = new StringBuilder();
					boolean first = true;
					for (int key : d_select_map.keySet()) {
						Button cb = d_select_map.get(key);
						if (cb.getSelection()) {
							if (!first) {
								buf.append(",");
							}
							buf.append(key);
							first = false;
						}
					}
					dateData.put("d", buf.toString());
					if (buf.length() == 0) {
						MessageDialog.openError(shell, "错误", "没有指定天！");
						return;
					}
				}
			}

			if (mTriggerButton.getSelection()) {// 月份
				dateData.put("m", "*");
			} else {
				StringBuilder buf = new StringBuilder();
				boolean first = true;
				for (int key : m_select_map.keySet()) {
					Button cb = m_select_map.get(key);
					if (cb.getSelection()) {
						if (!first) {
							buf.append(",");
						}
						buf.append(key);
						first = false;
					}
				}
				dateData.put("m", buf.toString());
				if (buf.length() == 0) {
					MessageDialog.openError(shell, "错误", "没有指定月！");
					return;
				}
			}

			String cronStr = dateData.get("s") + " " + dateData.get("min")
					+ " " + dateData.get("h") + " " + dateData.get("d") + " "
					+ dateData.get("m") + " " + dateData.get("w");

			CronExpression exp = new CronExpression(cronStr);
			expressionText.setText(exp.toString());

			performTimeText.setText("");// 执行时间设置为空
			java.util.Date dd = new java.util.Date();
			startTimeText.setText(DateFormatUtil.format("yyyy-MM-dd HH:mm:ss",
					dd));
			for (int i = 1; i <= 8; i++) {
				dd = exp.getNextValidTimeAfter(dd);
				performTimeText.append(i + ": "
						+ DateFormatUtil.format("yyyy-MM-dd HH:mm:ss", dd)
						+ "\n");
				dd = new java.util.Date(dd.getTime() + 1000);
			}

		} catch (Exception e2) {
			// TODO 记录日志
			MessageDialog.openError(shell, "错误", "生成表达式过程出现错误！");
			return;
		}
	}

	/**
	 * 将表达式解析到界面上
	 * 
	 * @param e
	 */
	public void parseToUI(SelectionEvent e) {
		try {
			if (expressionText.getText().trim().length() == 0) {
				return;
			}

			performTimeText.setText("");
			CronExpression exp = new CronExpression(expressionText.getText()
					.trim());
			Date dd = new Date();
			startTimeText.setText(DateFormatUtil.format("yyyy-MM-dd HH:mm:ss",
					dd));
			for (int i = 1; i <= 8; i++) {
				dd = exp.getNextValidTimeAfter(dd);
				performTimeText.append(i + ": "
						+ DateFormatUtil.format("yyyy-MM-dd HH:mm:ss", dd)
						+ "\n");
				dd = new java.util.Date(dd.getTime() + 1000);
			}

			String expression = exp.getCronExpression();
			String[] expressionArrays = expression.split(" ");
			dateData.put("s", expressionArrays[0]);
			dateData.put("min", expressionArrays[1]);
			dateData.put("h", expressionArrays[2]);
			dateData.put("d", expressionArrays[3]);
			dateData.put("m", expressionArrays[4]);
			dateData.put("w", expressionArrays[5]);

			// 秒
			for (Button cb : s_select_map.values()) {
				cb.setSelection(false);
			}
			String expValues = expressionArrays[0];

			if (expValues.contains("*")) { // 是'*'
				sTriggerButton.setSelection(true);
			} else {
				if (dateData.get("s").contains("/")) {
					sCycleButton.setSelection(true);
					Integer iFrom = Integer.valueOf(expValues.split("/")[0]);
					Integer iTo = Integer.valueOf(expValues.split("/")[1]);
					int interval = iTo - iFrom;
					sComboStart.setText(String.valueOf(iFrom));
					sComboEvery.setText(String.valueOf(interval));
				} else {
					sSpecifyButton.setSelection(true);
					String[] strs = expValues.split(",");
					for (String str : strs) {
						s_select_map.get(Integer.valueOf(str)).setSelection(
								true);
					}
				}
			}

			// 分钟
			for (Button cb : min_select_map.values()) {
				cb.setSelection(false);
			}
			expValues = expressionArrays[1];
			if (expValues.contains("*")) { // 是'*'
				minTriggerButton.setSelection(true);
			} else {
				if (dateData.get("min").contains("/")) {
					minCycleButton.setSelection(true);
					Integer iFrom = Integer.valueOf(expValues.split("/")[0]);
					Integer iTo = Integer.valueOf(expValues.split("/")[1]);
					int interval = iTo - iFrom;
					minComboStart.setText(String.valueOf(iFrom));
					minComboEvery.setText(String.valueOf(interval));
				} else {
					minSpecifyButton.setSelection(true);
					String[] strs = expValues.split(",");
					for (String str : strs) {
						min_select_map.get(Integer.valueOf(str)).setSelection(
								true);
					}
				}
			}

			// 小时
			for (Button cb : h_select_map.values()) {
				cb.setSelection(false);
			}

			expValues = expressionArrays[2];
			if (expValues.contains("*")) { // 是'*'
				hTriggerButton.setSelection(true);
			} else {
				hSpecifyButton.setSelection(true);

				String[] strs = expValues.split(",");
				for (String str : strs) {
					h_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

			// 天
			for (Button cb : d_select_map.values()) {
				cb.setSelection(false);
			}

			expValues = expressionArrays[3];
			if (expValues.contains("?")) { // 是'?'
				// 不操作
			} else if (expValues.contains("*")) { // 是'*'
				dTriggerButton.setSelection(true);
			} else {
				dSpecifyButton.setSelection(true);
				String[] strs = expValues.split(",");
				for (String str : strs) {
					d_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

			// 月
			for (Button cb : m_select_map.values()) {
				cb.setSelection(false);
			}

			expValues = expressionArrays[4];
			if (expValues.contains("*")) { // 是'*'
				mTriggerButton.setSelection(true);
			} else {
				mSpecifyButton.setSelection(true);

				String[] strs = expValues.split(",");
				for (String str : strs) {
					m_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

			// 周
			for (Button cb : w_select_map.values()) {
				cb.setSelection(false);
			}
			wConfigurationButton.setSelection(true);

			expValues = expressionArrays[5];
			if (expValues.contains("?")) { // 是'?'
				wConfigurationButton.setSelection(true);
				wTriggerButton.setSelection(false);
				wSpecifyButton.setSelection(false);
			} else if (expValues.contains("*")) { // 是'*'
				wConfigurationButton.setSelection(true);
				wTriggerButton.setSelection(true);
			} else {
				wConfigurationButton.setSelection(true);
				wSpecifyButton.setSelection(true);

				String[] strs = expValues.split(",");
				for (String str : strs) {
					m_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

		} catch (Throwable e1) {
			// TODO 添加日志
			MessageDialog.openError(shell, "错误", "解析到界面过程出错！");
			return;
		}
	}

	// 秒
	private Button s_select_0;
	private Button s_select_1;
	private Button s_select_2;
	private Button s_select_3;
	private Button s_select_4;
	private Button s_select_5;
	private Button s_select_6;
	private Button s_select_7;
	private Button s_select_8;
	private Button s_select_9;
	private Button s_select_10;
	private Button s_select_11;
	private Button s_select_12;
	private Button s_select_13;
	private Button s_select_14;
	private Button s_select_15;
	private Button s_select_16;
	private Button s_select_17;
	private Button s_select_18;
	private Button s_select_19;
	private Button s_select_20;
	private Button s_select_21;
	private Button s_select_22;
	private Button s_select_23;
	private Button s_select_24;
	private Button s_select_25;
	private Button s_select_26;
	private Button s_select_27;
	private Button s_select_28;
	private Button s_select_29;
	private Button s_select_30;
	private Button s_select_31;
	private Button s_select_32;
	private Button s_select_33;
	private Button s_select_34;
	private Button s_select_35;
	private Button s_select_36;
	private Button s_select_37;
	private Button s_select_38;
	private Button s_select_39;
	private Button s_select_40;
	private Button s_select_41;
	private Button s_select_42;
	private Button s_select_43;
	private Button s_select_44;
	private Button s_select_45;
	private Button s_select_46;
	private Button s_select_47;
	private Button s_select_48;
	private Button s_select_49;
	private Button s_select_50;
	private Button s_select_51;
	private Button s_select_52;
	private Button s_select_53;
	private Button s_select_54;
	private Button s_select_55;
	private Button s_select_56;
	private Button s_select_57;
	private Button s_select_58;
	private Button s_select_59;
	// 分
	private Button min_select_0;
	private Button min_select_1;
	private Button min_select_2;
	private Button min_select_3;
	private Button min_select_4;
	private Button min_select_5;
	private Button min_select_6;
	private Button min_select_7;
	private Button min_select_8;
	private Button min_select_9;
	private Button min_select_10;
	private Button min_select_11;
	private Button min_select_12;
	private Button min_select_13;
	private Button min_select_14;
	private Button min_select_15;
	private Button min_select_16;
	private Button min_select_17;
	private Button min_select_18;
	private Button min_select_19;
	private Button min_select_20;
	private Button min_select_21;
	private Button min_select_22;
	private Button min_select_23;
	private Button min_select_24;
	private Button min_select_25;
	private Button min_select_26;
	private Button min_select_27;
	private Button min_select_28;
	private Button min_select_29;
	private Button min_select_30;
	private Button min_select_31;
	private Button min_select_32;
	private Button min_select_33;
	private Button min_select_34;
	private Button min_select_35;
	private Button min_select_36;
	private Button min_select_37;
	private Button min_select_38;
	private Button min_select_39;
	private Button min_select_40;
	private Button min_select_41;
	private Button min_select_42;
	private Button min_select_43;
	private Button min_select_44;
	private Button min_select_45;
	private Button min_select_46;
	private Button min_select_47;
	private Button min_select_48;
	private Button min_select_49;
	private Button min_select_50;
	private Button min_select_51;
	private Button min_select_52;
	private Button min_select_53;
	private Button min_select_54;
	private Button min_select_55;
	private Button min_select_56;
	private Button min_select_57;
	private Button min_select_58;
	private Button min_select_59;
	// 时
	private Button h_select_0;
	private Button h_select_1;
	private Button h_select_2;
	private Button h_select_3;
	private Button h_select_4;
	private Button h_select_5;
	private Button h_select_6;
	private Button h_select_7;
	private Button h_select_8;
	private Button h_select_9;
	private Button h_select_10;
	private Button h_select_11;
	private Button h_select_12;
	private Button h_select_13;
	private Button h_select_14;
	private Button h_select_15;
	private Button h_select_16;
	private Button h_select_17;
	private Button h_select_18;
	private Button h_select_19;
	private Button h_select_20;
	private Button h_select_21;
	private Button h_select_22;
	private Button h_select_23;
	// 天
	private Button d_select_1;
	private Button d_select_2;
	private Button d_select_3;
	private Button d_select_4;
	private Button d_select_5;
	private Button d_select_6;
	private Button d_select_7;
	private Button d_select_8;
	private Button d_select_9;
	private Button d_select_10;
	private Button d_select_11;
	private Button d_select_12;
	private Button d_select_13;
	private Button d_select_14;
	private Button d_select_15;
	private Button d_select_16;
	private Button d_select_17;
	private Button d_select_18;
	private Button d_select_19;
	private Button d_select_20;
	private Button d_select_21;
	private Button d_select_22;
	private Button d_select_23;
	private Button d_select_24;
	private Button d_select_25;
	private Button d_select_26;
	private Button d_select_27;
	private Button d_select_28;
	private Button d_select_29;
	private Button d_select_30;
	private Button d_select_31;
	// 月
	private Button m_select_1;
	private Button m_select_2;
	private Button m_select_3;
	private Button m_select_4;
	private Button m_select_5;
	private Button m_select_6;
	private Button m_select_7;
	private Button m_select_8;
	private Button m_select_9;
	private Button m_select_10;
	private Button m_select_11;
	private Button m_select_12;
	// 周
	private Button w_select_0;
	private Button w_select_1;
	private Button w_select_2;
	private Button w_select_3;
	private Button w_select_4;
	private Button w_select_5;
	private Button w_select_6;

}
