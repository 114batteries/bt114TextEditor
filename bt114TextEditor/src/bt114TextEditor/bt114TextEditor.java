package bt114TextEditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class bt114TextEditor {

	static String filename;
	static Display dp;
	static Shell sh;
	static final String charset="UTF-8";

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		bt114TextEditor te=new bt114TextEditor();
		if(args.length==0) {
			te.start(null);
		}
		else {
			te.start(args[0]);
		}
	}

	public void start(String fn){

		filename=fn;

		dp=new Display();
		sh=new Shell(dp);
		sh.setLayout(new FormLayout());
		sh.setSize(400,400);

		ToolBar tb=new ToolBar(sh,SWT.WRAP);
		FormData fd_tb=new FormData();
		fd_tb.top=new FormAttachment(0,0);
		fd_tb.left=new FormAttachment(0,0);
		fd_tb.right=new FormAttachment(100,0);
		tb.setLayoutData(fd_tb);

		ToolItem ti_new=new ToolItem(tb,SWT.PUSH);
		ti_new.setText("新規");

		ToolItem ti_open=new ToolItem(tb,SWT.PUSH);
		ti_open.setText("開く");

		ToolItem ti_save=new ToolItem(tb,SWT.PUSH);
		ti_save.setText("保存");

		ToolItem ti_saveas=new ToolItem(tb,SWT.PUSH);
		ti_saveas.setText("名前を付けて保存");

		Text tf=new Text(sh,SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL);
		FormData fd_tf=new FormData();
		fd_tf.top=new FormAttachment(tb,0);
		fd_tf.bottom=new FormAttachment(100,0);
		fd_tf.left=new FormAttachment(0,0);
		fd_tf.right=new FormAttachment(100,0);
		tf.setLayoutData(fd_tf);

		sh.open();

		SelectionAdapter sa_new=new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openNew(null);
			}
		};
		ti_new.addSelectionListener(sa_new);

		SelectionAdapter sa_open=new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				FileDialog fd=new FileDialog(sh,SWT.OPEN);
				fd.open();
				String dir=fd.getFilterPath();
				dir+="\\"+fd.getFileName();
				tf.setText(openFile(dir));
			}
		};
		ti_open.addSelectionListener(sa_open);

		SelectionAdapter sa_save=new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveFile(tf.getText(),filename);
			}
		};
		ti_save.addSelectionListener(sa_save);

		SelectionAdapter sa_saveas=new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveFile(tf.getText(),null);
			}
		};
		ti_saveas.addSelectionListener(sa_saveas);

		if(filename!=null) {
			tf.setText(openFile(filename));
			sh.setText(filename);
		}
		else {
			sh.setText("新規ファイル");
		}

		DropTarget ddt=new DropTarget(sh,DND.DROP_COPY|DND.DROP_DEFAULT);
		FileTransfer transfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[]{transfer};
		ddt.setTransfer(types);
		ddt.addDropListener(new DropTargetAdapter() {
			@Override
			public void dragEnter(DropTargetEvent evt){
				evt.detail = DND.DROP_COPY;
			}

			public void drop(DropTargetEvent evt){
				String[] files = (String[])evt.data;
				for(int i=0;i<files.length;i++){
			    	openNew(files[i]);
			    }
			}
		});

		while (!sh.isDisposed()) {
			if (!dp.readAndDispatch()) dp.sleep();
		}
		dp.dispose();
	}

	public String openFile(String dir) {
		String result="";
		try {
			FileInputStream file = new FileInputStream(dir);
			InputStreamReader is = new InputStreamReader(file,charset);
			BufferedReader br = new BufferedReader(is);
			String str;
			while((str = br.readLine()) != null){
				result=result+str+"\r\n";
			}
			br.close();
			filename=dir;
			sh.setText(filename);
		}catch(FileNotFoundException fe) {
			System.out.println(fe);
		}catch(IOException ie) {
			System.out.println(ie);
		}
		return result;
	}

	public void saveFile(String output,String dir) {
		if(dir==null) {
			FileDialog fd=new FileDialog(sh,SWT.SAVE);
			fd.open();
			dir=fd.getFilterPath();
			dir+="\\"+fd.getFileName();
		}
		try {
			FileOutputStream file = new FileOutputStream(dir);
        	OutputStreamWriter ow = new OutputStreamWriter(file,charset);
        	BufferedWriter bw = new BufferedWriter(ow);
			bw.write(output);
			bw.close();
		}catch(FileNotFoundException fe) {
			System.out.println(fe);
		}catch(IOException ie) {
			System.out.println(ie);
		}
	}

	public void openNew(String fn) {
		String command="Java -jar bt114TextEditor.jar ";
		if(fn!=null) {
			command+=fn;
		}
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec(command);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
