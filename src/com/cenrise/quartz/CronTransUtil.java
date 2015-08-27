package com.cenrise.quartz;

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
/**
 * �Զ�����cron���ʽ
 *  public String cronTrans() {
    	String open = "";
    	try {
			CronTransUtil cronTransUtil = new CronTransUtil();
			open = cronTransUtil.open(shell);
		} catch (Exception e) {
			// TODO log
			new ErrorDialog(shell, "Error", "Error generator expression! :", e);
		}
		return open;
	}
 * @author admin
 *
 */
public class CronTransUtil {

	private static CTabFolder CTabFolder;

	private Map<Integer, Button> s_select_map = new TreeMap<Integer, Button>();// ��ѡ��
	private Map<Integer, Button> min_select_map = new TreeMap<Integer, Button>();// ��ѡ��
	private Map<Integer, Button> h_select_map = new TreeMap<Integer, Button>();// ʱѡ��
	private Map<Integer, Button> d_select_map = new TreeMap<Integer, Button>();// ��ѡ��
	private Map<Integer, Button> w_select_map = new TreeMap<Integer, Button>();// ��ѡ��
	private Map<Integer, Button> m_select_map = new TreeMap<Integer, Button>();// ��ѡ��
	// ��ť
	private Button sTriggerButton, sCycleButton, sSpecifyButton;
	private CCombo sComboEvery, sComboStart;
	private Button minTriggerButton, minCycleButton, minSpecifyButton;
	private CCombo minComboEvery, minComboStart;
	private Button hTriggerButton, hSpecifyButton;
	private Button dTriggerButton, dSpecifyButton;
	private Button mTriggerButton, mSpecifyButton;
	private Button wConfigurationButton, wTriggerButton, wSpecifyButton;
	private Map<String, String> dateData = new HashMap<String, String>();// ѡ������ݼ���

	private Text expressionText;// ���ɵı��ʽ��ʾ��
	private Text startTimeText;// ��ʼʱ��
	private Text performTimeText;// ִ��ʱ��

	private Shell shell;
	private Display display;
	private String cronExpressionReturn;// open�ķ���ֵ
	static Text performTimeText2;// ���뷵�ص�ֵ

	/**
	 * �򿪴���.
	 */
	public String open(Shell cronShell) {
		final Shell shell = new Shell(cronShell, SWT.CLOSE | SWT.MIN);
		this.shell = shell;
		// ��ȡ��ǰ��ʾ���ֱ���
		Rectangle area = Display.getDefault().getClientArea();
		shell.setLocation(area.width * 1 / 6, area.height * 1 / 10);
		shell.setSize(700, 600);
		shell.setText("Cron���ʽ");
		createContents();
		shell.open();
		// shell.pack();//���pack����������
		shell.layout();
		display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// display.dispose();// ����ע��Display
		return cronExpressionReturn;
	}

	// ��ʼ������
	private void createContents() {
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		shell.setLayout(formLayout);

		// �ϱ߿򲼾�
		CTabFolder = new CTabFolder(shell, SWT.BORDER);
		CTabFolder.setFocus();

		FormData CTabFolderFormData = new FormData();
		CTabFolderFormData.top = new FormAttachment(0, 0);
		CTabFolderFormData.bottom = new FormAttachment(40, 0);
		CTabFolderFormData.left = new FormAttachment(0, 0);
		CTabFolderFormData.right = new FormAttachment(100, 0);
		CTabFolder.setLayoutData(CTabFolderFormData);

		// ��
		CTabItem sCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		sCTabItem.setText("��");

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
		sTriggerButton.setText("ÿ�봥��");
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
		sCycleButton.setText("ѭ�� ");
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
		sCLabelCycle1.setText("��");
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
		sCLabelCycle2.setText("�뿪ʼ��ÿ");

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
		sCLabelCycle3.setText("�봥��");

		FormAttachment sComboEveryFormAttachment = new FormAttachment(
				sComboEvery);
		FormData sCLabelCycle3FormData = new FormData();
		sCLabelCycle3FormData.top = new FormAttachment(16, 0);
		sCLabelCycle3FormData.bottom = new FormAttachment(26, 0);
		sCLabelCycle3FormData.left = sComboEveryFormAttachment;
		sCLabelCycle3FormData.right = new FormAttachment(44, 0);
		sCLabelCycle3.setLayoutData(sCLabelCycle3FormData);

		sSpecifyButton = new Button(sComposite, SWT.RADIO);
		sSpecifyButton.setText("ָ��");
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

				} else if (selected == false) {
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

		for (int i = 0; i < 60; i++) {
			Button button = new Button(sSelectGroup, SWT.CHECK);
			button.setText(i + "");
			s_select_map.put(i, button);

			button.setSelection(false);
			button.setEnabled(false);
		}

		// ��
		CTabItem minCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		minCTabItem.setText("��");

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
		minTriggerButton.setText("ÿ���Ӵ���");
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
		minCycleButton.setText("ѭ�� ");
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
		minCLabelCycle1.setText("��");
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
		minCLabelCycle2.setText("�ֿ�ʼ��ÿ");
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
		minCLabelCycle3.setText("�ִ���");

		FormData minCLabelCycle3FormData = new FormData();
		minCLabelCycle3FormData.top = new FormAttachment(16, 0);
		minCLabelCycle3FormData.bottom = new FormAttachment(26, 0);
		minCLabelCycle3FormData.left = minComboEveryFormAttachment;
		minCLabelCycle3FormData.right = new FormAttachment(45, 0);
		minCLabelCycle3.setLayoutData(minCLabelCycle3FormData);

		minSpecifyButton = new Button(minComposite, SWT.RADIO);
		minSpecifyButton.setText("ָ��");
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

		for (int i = 0; i < 60; i++) {
			Button button = new Button(minSelectGroup, SWT.CHECK);
			button.setText(i + "");
			min_select_map.put(i, button);

			button.setSelection(false);
			button.setEnabled(false);
		}

		// Сʱ
		CTabItem hCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		hCTabItem.setText("Сʱ");

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
		hTriggerButton.setText("ÿСʱ����");
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
		hSpecifyButton.setText("ָ��");
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

		for (int i = 0; i < 24; i++) {
			Button button = new Button(hSelectGroup, SWT.CHECK);
			button.setText(i + "");
			h_select_map.put(i, button);

			button.setSelection(false);
			button.setEnabled(false);
		}

		// �տ�ʼ
		CTabItem dCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		dCTabItem.setText("��");

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
		dTriggerButton.setText("ÿ�촥��");
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
		dSpecifyButton.setText("ָ��");
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

		for (int i = 1; i < 32; i++) {
			Button button = new Button(dSelectGroup, SWT.CHECK);
			button.setText(i + "");
			d_select_map.put(i, button);

			button.setSelection(false);
			button.setEnabled(false);
		}

		// �¿�ʼ
		CTabItem mCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		mCTabItem.setText("��");

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
		mTriggerButton.setText("ÿ�´���");
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
		mSpecifyButton.setText("ָ��");
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

		for (int i = 1; i < 13; i++) {
			Button button = new Button(mSelectGroup, SWT.CHECK);
			button.setText(i + "");
			m_select_map.put(i, button);

			button.setSelection(false);
			button.setEnabled(false);
		}

		// �ܿ�ʼ
		CTabItem wCTabItem = new CTabItem(CTabFolder, SWT.NONE);
		wCTabItem.setText("��");

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
		wConfigurationButton.setText("ʹ��������");
		wConfigurationButton.setSelection(false);
		wConfigurationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = wConfigurationButton.getSelection();
				if (selected == true) {
					wConfigurationButton.setSelection(true);
					wTriggerButton.setEnabled(true);
					wSpecifyButton.setEnabled(true);
					wTriggerButton.setSelection(false);
					wSpecifyButton.setSelection(false);
					for (Button cb : w_select_map.values()) {
						cb.setEnabled(!selected);
					}
				}
				if (selected == false) {
					wConfigurationButton.setSelection(false);
					wTriggerButton.setEnabled(false);
					wSpecifyButton.setEnabled(false);
					wTriggerButton.setSelection(false);
					wSpecifyButton.setSelection(false);
					for (Button cb : w_select_map.values()) {
						cb.setEnabled(selected);
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
		wTriggerButton.setText("ÿ�ܴ���");
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
		wSpecifyButton.setText("ָ��");
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

		for (int i = 0; i < 7; i++) {
			Button button = new Button(wSelectGroup, SWT.CHECK);
			switch (i) {
			case 0:button.setText("��һ");break;
			case 1:button.setText("�ܶ�");break;
			case 2:button.setText("����");break;
			case 3:button.setText("����");break;
			case 4:button.setText("����");break;
			case 5:button.setText("����");break;
			case 6:button.setText("����");break;
			}
			w_select_map.put(i + 1, button);
			button.setSelection(false);
			button.setEnabled(false);
		}

		// �м䰴ť
		Composite centerComposite = new Composite(shell, SWT.NONE);
		centerComposite.setLayout(new FormLayout());
		FormData centerCompositeFormData = new FormData();
		centerCompositeFormData.top = new FormAttachment(40, 0);
		centerCompositeFormData.bottom = new FormAttachment(45, 0);
		centerCompositeFormData.left = new FormAttachment(0, 0);
		centerCompositeFormData.right = new FormAttachment(100, 0);
		centerComposite.setLayoutData(centerCompositeFormData);

		Button centerButton = new Button(centerComposite, SWT.NONE);
		centerButton.setText("���ɱ��ʽ");
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

		// 3�±߿򲼾�
		Group downGroup = new Group(shell, SWT.NONE);
		downGroup.setText("���ʽ");
		downGroup.setLayout(new FormLayout());

		FormData expressionGroupFormData = new FormData();
		expressionGroupFormData.top = new FormAttachment(45, 0);
		expressionGroupFormData.bottom = new FormAttachment(94, 0);
		expressionGroupFormData.left = new FormAttachment(0, 0);
		expressionGroupFormData.right = new FormAttachment(100, 0);
		downGroup.setLayoutData(expressionGroupFormData);

		CLabel expressionCLabel = new CLabel(downGroup, SWT.NONE);
		expressionCLabel.setText("Cron���ʽ:");
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
		buttonResolutionToView.setText("����������");
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
		executionTimePlanGroup.setText("ִ��ʱ��ƻ�");
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
		startTimeCLabel.setText("��ʼʱ��:");

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
		performTimeCLabel.setText("ִ��ʱ��:");

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

		// 4 ȷ��ȡ����ť
		Composite bottomComposite = new Composite(shell, SWT.NONE);
		bottomComposite.setLayout(new FormLayout());
		FormData bottomCompositeFormData = new FormData();
		bottomCompositeFormData.top = new FormAttachment(94, 0);
		bottomCompositeFormData.bottom = new FormAttachment(100, 0);
		bottomCompositeFormData.left = new FormAttachment(0, 0);
		bottomCompositeFormData.right = new FormAttachment(100, 0);
		bottomComposite.setLayoutData(bottomCompositeFormData);

		Button confirmButton = new Button(bottomComposite, SWT.NONE);
		confirmButton.setText("ȷ��");
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
				// shell.setVisible(false);
			}
		});

		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION
						| SWT.YES | SWT.NO);
				messagebox.setText("��ʾ");
				messagebox.setMessage("��ȷ��Ҫ�رղ�����?");
				// int message = messagebox.open();
				// e.doit = message == SWT.YES;
			}
		});

		Button cancelButton = new Button(bottomComposite, SWT.NONE);
		cancelButton.setText("ȡ��");
		FormData cancelButtonFormData = new FormData();
		cancelButtonFormData.top = new FormAttachment(20, 0);
		cancelButtonFormData.bottom = new FormAttachment(80, 0);
		cancelButtonFormData.left = new FormAttachment(60, 0);
		cancelButtonFormData.right = new FormAttachment(70, 0);
		cancelButton.setLayoutData(cancelButtonFormData);

		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
			}
		});
	}

	/**
	 * ����Cron���ʽ
	 * 
	 * @param evt
	 */
	private void generateCronExpression(SelectionEvent e) {
		// ��������
		try {
			if (sTriggerButton.getSelection()) {
				dateData.put("s", "*");
			} else if (sCycleButton.getSelection()) {
				dateData.put("s", String.valueOf(sComboStart.getText()) + "/"
						+ String.valueOf(sComboEvery.getText()));
			} else {
				StringBuilder buf = new StringBuilder();
				boolean first = true;
				// �������ϰ�ѡ�еķ��ӷ���StringBuilder
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
					MessageDialog.openError(shell, "����", "û��ָ���룡");
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
				// �������ϰ�ѡ�еķ��ӷ���StringBuilder
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
					MessageDialog.openError(shell, "����", "û��ָ���֣�");
					return;
				}
			}

			if (hTriggerButton.getSelection()) {
				dateData.put("h", String.valueOf("*"));
			} else {
				StringBuilder buf = new StringBuilder();
				boolean first = true;
				// �������ϰ�ѡ�еķ��ӷ���StringBuilder
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
					MessageDialog.openError(shell, "����", "û��ָ��ʱ��");
					return;
				}
			}

			if (wConfigurationButton.getSelection()) {// �ܣ��ܺ���ֻ��ѡ��һ��
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
						MessageDialog.openError(shell, "����", "û��ָ���ܣ�");
						return;
					}

				}
			} else {
				dateData.put("w", "?");
				if (dTriggerButton.getSelection()) {// ��
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
						MessageDialog.openError(shell, "����", "û��ָ���죡");
						return;
					}
				}
			}

			if (mTriggerButton.getSelection()) {// �·�
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
					MessageDialog.openError(shell, "����", "û��ָ���£�");
					return;
				}
			}

			String cronStr = dateData.get("s") + " " + dateData.get("min")
					+ " " + dateData.get("h") + " " + dateData.get("d") + " "
					+ dateData.get("m") + " " + dateData.get("w");

			CronExpression exp = new CronExpression(cronStr);
			expressionText.setText(exp.toString());

			performTimeText.setText("");// ִ��ʱ������Ϊ��
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
			// TODO ��¼��־
			MessageDialog.openError(shell, "����", "���ɱ��ʽ���̳��ִ���");
			return;
		}
	}

	/**
	 * �����ʽ������������
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

			// ��
			for (Button cb : s_select_map.values()) {
				cb.setSelection(false);
			}
			String expValues = expressionArrays[0];

			if (expValues.contains("*")) { // ��'*'
				sTriggerButton.setSelection(true);
				// ѭ����ָ��ȡ��ѡ��
				sCycleButton.setSelection(false);
				sSpecifyButton.setSelection(false);
			} else {
				if (dateData.get("s").contains("/")) {
					sCycleButton.setSelection(true);
					// ������ָ��ȡ��ѡ��
					sTriggerButton.setSelection(false);
					sSpecifyButton.setSelection(false);
					// ָ��������
					sComboStart.setEnabled(false);
					sComboEvery.setEnabled(false);

					Integer iFrom = Integer.valueOf(expValues.split("/")[0]);
					Integer iTo = Integer.valueOf(expValues.split("/")[1]);
					int interval = iTo - iFrom;

					// ָ������
					sComboStart.setEnabled(true);
					sComboEvery.setEnabled(true);
					// ����ָ��ֵ
					sComboStart.setText(String.valueOf(iFrom));
					sComboEvery.setText(String.valueOf(interval));
				} else {
					sSpecifyButton.setEnabled(true);
					sSpecifyButton.setSelection(true);
					// ������ѭ��������
					sTriggerButton.setSelection(false);
					sCycleButton.setSelection(false);
					// ָ��������
					sComboStart.setEnabled(false);
					sComboEvery.setEnabled(false);

					// �������ÿ���
					for (Button cb : s_select_map.values()) {
						cb.setEnabled(true);
					}
					// ����ָ����ť
					String[] strs = expValues.split(",");
					for (String str : strs) {
						s_select_map.get(Integer.valueOf(str)).setSelection(
								true);
					}
				}
			}

			// ����
			for (Button cb : min_select_map.values()) {
				cb.setSelection(false);
			}
			expValues = expressionArrays[1];
			if (expValues.contains("*")) { // ��'*'
				minTriggerButton.setSelection(true);

				minCycleButton.setSelection(false);
				minSpecifyButton.setSelection(false);

				minComboStart.setEnabled(false);
				minComboEvery.setEnabled(false);
			} else {
				if (dateData.get("min").contains("/")) {
					minCycleButton.setSelection(true);
					// ������ָ��������
					minTriggerButton.setSelection(false);
					minSpecifyButton.setSelection(false);

					Integer iFrom = Integer.valueOf(expValues.split("/")[0]);
					Integer iTo = Integer.valueOf(expValues.split("/")[1]);
					int interval = iTo - iFrom;

					// ָ����ť����
					minComboStart.setEnabled(true);
					minComboEvery.setEnabled(true);
					// �༭ָ��ֵ
					minComboStart.setText(String.valueOf(iFrom));
					minComboEvery.setText(String.valueOf(interval));
				} else {
					minSpecifyButton.setSelection(true);
					// ������ѭ��������
					minTriggerButton.setSelection(false);
					minCycleButton.setSelection(false);
					// ָ����ť������
					minComboStart.setEnabled(false);
					minComboEvery.setEnabled(false);

					for (Button cb : min_select_map.values()) {
						cb.setEnabled(true);
					}
					String[] strs = expValues.split(",");
					for (String str : strs) {
						min_select_map.get(Integer.valueOf(str)).setSelection(
								true);
					}
				}
			}

			// Сʱ
			for (Button cb : h_select_map.values()) {
				cb.setSelection(false);
			}

			expValues = expressionArrays[2];
			if (expValues.contains("*")) { // ��'*'
				hTriggerButton.setSelection(true);
				hSpecifyButton.setSelection(false);
			} else {
				hSpecifyButton.setSelection(true);
				hTriggerButton.setSelection(false);

				for (Button cb : h_select_map.values()) {
					cb.setEnabled(true);
				}

				String[] strs = expValues.split(",");
				for (String str : strs) {
					h_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

			// ��
			for (Button cb : d_select_map.values()) {
				cb.setSelection(false);
			}

			expValues = expressionArrays[3];
			if (expValues.contains("?")) { // ��'?'
				// ������
			} else if (expValues.contains("*")) { // ��'*'
				dTriggerButton.setSelection(true);
				dSpecifyButton.setSelection(false);
			} else {
				dSpecifyButton.setSelection(true);
				dTriggerButton.setSelection(false);

				for (Button cb : d_select_map.values()) {
					cb.setEnabled(true);
				}

				String[] strs = expValues.split(",");
				for (String str : strs) {
					d_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

			// ��
			for (Button cb : m_select_map.values()) {
				cb.setSelection(false);
			}

			expValues = expressionArrays[4];
			if (expValues.contains("*")) { // ��'*'
				mTriggerButton.setSelection(true);
				mSpecifyButton.setSelection(false);
			} else {
				mSpecifyButton.setSelection(true);
				mTriggerButton.setSelection(false);

				for (Button cb : m_select_map.values()) {
					cb.setEnabled(true);
				}

				String[] strs = expValues.split(",");
				for (String str : strs) {
					m_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

			// ��
			for (Button cb : w_select_map.values()) {
				cb.setSelection(false);
			}
			wConfigurationButton.setSelection(true);

			expValues = expressionArrays[5];
			if (expValues.contains("?")) { // ��'?'
				wConfigurationButton.setSelection(false);
				wTriggerButton.setSelection(false);
				wSpecifyButton.setSelection(false);

				for (Button cb : w_select_map.values()) {
					cb.setEnabled(false);
				}
			} else if (expValues.contains("*")) { // ��'*'
				wConfigurationButton.setSelection(true);
				wTriggerButton.setSelection(true);
				wSpecifyButton.setSelection(false);
			} else {
				wConfigurationButton.setSelection(true);
				wSpecifyButton.setEnabled(true);
				wSpecifyButton.setSelection(true);
				wTriggerButton.setSelection(false);

				for (Button cb : w_select_map.values()) {
					cb.setEnabled(true);
				}

				String[] strs = expValues.split(",");
				for (String str : strs) {
					w_select_map.get(Integer.valueOf(str)).setSelection(true);
				}
			}

		} catch (Throwable e1) {
			// TODO �����־
			MessageDialog.openError(shell, "����", "������������̳���");
			return;
		}
	}

	/**
	 * ���� �����ť���������ڣ�ͨ������ȷ�Ϻ󷵻�ֵ��
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
				CronTransUtil d = new CronTransUtil();
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

		while (!shell2.isDisposed()) {// ���鴰���Ƿ�ر�
			if (!display.readAndDispatch()) // ����display�߳�״̬�Ƿ�æ
				display.sleep();// Display���߳�����
		}
		display.dispose();// ע��Display������Դ
	}
}
