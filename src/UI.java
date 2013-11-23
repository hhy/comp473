package proj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class UI extends JPanel implements MouseInputListener {
	int w, h;
	List<List<Integer>> xTracks, yTracks;
	boolean mousedown = false, startDraw = false, finishDraw = false;

	public UI(int w, int h) {
		super();
		this.w = w;
		this.h = h;
		this.xTracks = new ArrayList<List<Integer>>();
		this.yTracks = new ArrayList<List<Integer>>();

		this.addMouseMotionListener(this);
		this.addMouseListener(this);

	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < xTracks.size(); i++) {
			List<Integer> xT = xTracks.get(i), yT = yTracks.get(i);
			int[] x = new int[xT.size()], y = new int[yT.size()];
			for (int j = 0; j < x.length; j++) {
				x[j] = xT.get(j);
				y[j] = yT.get(j);
			}
			
			g.drawPolyline(x, y, x.length);

			g.drawChars(new char[]{'n'}, 0,  1, 15, 15);
			
		}

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.w, this.h);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	private List<Integer> xl=new ArrayList<Integer>(), yl=new ArrayList<Integer>();
	@Override
	public void mousePressed(MouseEvent e) {
		this.mousedown = true;
		xl.clear();yl.clear();
		Point p=e.getPoint();		
		xl.add(p.x); yl.add(p.y);
	

	}
	
	

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mousedown = false;
		Point p=e.getPoint();		
		xl.add(p.x); yl.add(p.y);
		
		this.xTracks.add(xl);
		this.yTracks.add(yl);
		this.repaint();	
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p=e.getPoint();		
		xl.add(p.x); yl.add(p.y);

		this.xTracks.add(xl);
		this.yTracks.add(yl);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	public static void main(String[] args){
		JFrame jf=new JFrame("digit");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UI ui=new UI(400,400);
		jf.add(ui);
		jf.pack();
		jf.setVisible(true);
	}

}
