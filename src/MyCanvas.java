package proj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import proj.preprocess.Spline;

public class MyCanvas extends JPanel implements MouseInputListener {
	int w, h;

	List<List<Point>> lines;
	List<Point> line;

	Point pPre;
	boolean mousedown = false, startDraw = false, finishDraw = false;

	public MyCanvas(int w, int h) {
		super();
		this.w = w;
		this.h = h;

		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.lines = new ArrayList<List<Point>>();
		this.line = new ArrayList<Point>();

	}

	void interpolateLine(List<Point> line) {
		double[] xs = new double[line.size()], ys = new double[line.size()];
		for (int i = 0; i < line.size(); i++) {
			Point p = line.get(i);
			xs[i] = p.x;
			ys[i] = p.y;
		}
		Spline spl = new Spline(xs, ys);

		Point pStart = line.get(0), pEnd = line.get(line.size() - 1);
		if (pStart.x > pEnd.x) {
			Point p = pStart;
			pStart = pEnd;
			pEnd = p;
		}
		for (int x = pStart.x; x <= pEnd.x; x++) {
			line.add(new Point(x, (int) (spl.getValue(x))));
		}

		spl = new Spline(ys, xs);
		// newLine.clear();

		if (pStart.y > pEnd.y) {
			Point p = pStart;
			pStart = pEnd;
			pEnd = p;
		}
		for (int y = pStart.y; y <= pEnd.y; y++) {
			line.add(new Point((int) (spl.getValue(y)), y));
		}

	}

	public int[][] getMatrix() {

		int[][] m = new int[h][this.getHeight()];
		for (int y = 0; y < h; y++) {
			m[y] = new int[this.getWidth()];
			Arrays.fill(m[y], 0);
		}

		System.out.println(String.format("Real dimension [ %d x %d",
				this.getWidth(), this.getHeight()));

		for (List<Point> _line : this.lines) {
			// for(int i=0; i<1; i++)
			this.interpolateLine(_line);

			for (Point p : _line) {
				p.y -= this.getY();
				p.x -= this.getX();
				if (p.y >= this.getHeight() || p.x >= this.getWidth()
						|| p.y < 0 || p.x < 0)
					continue;
				m[p.y][p.x] = 1;
			}
		}
		this.thicken(m);
		this.thicken(m);
		this.thicken(m);
		
		

		return m;
	}

	void thicken(int[][] m) {
		for (int y = 0; y < m.length/2; y+=2)
			for (int x = 0; x < m[0].length/2; x+=2) {
				int xx=x, yy=y;
				if(m[yy][xx]==1) {
					m[yy+1][xx]=1;
					m[yy+1][xx-1]=1;
					m[yy+1][xx+1]=1;
					
					m[yy][xx+1]=1;
					m[yy][xx-1]=1;
					
					m[yy-1][xx+1]=1;
					m[yy-1][xx-1]=1;
					m[yy-1][xx]=1;
				}
				xx=m[0].length-1-x;
				yy=m.length-1-y;
				if(m[yy][xx]==1) {
					m[yy+1][xx]=1;
					m[yy+1][xx-1]=1;
					m[yy+1][xx+1]=1;
					
					m[yy][xx+1]=1;
					m[yy][xx-1]=1;
					
					m[yy-1][xx+1]=1;
					m[yy-1][xx-1]=1;
					m[yy-1][xx]=1;
				}
			}
		for (int y = 1; y < m.length/2; y+=2)
			for (int x = 1; x < m[0].length/2; x+=2) {
				int xx=x, yy=y;
				if(m[yy][xx]==1) {
					m[yy+1][xx]=1;
					m[yy+1][xx-1]=1;
					m[yy+1][xx+1]=1;
					
					m[yy][xx+1]=1;
					m[yy][xx-1]=1;
					
					m[yy-1][xx+1]=1;
					m[yy-1][xx-1]=1;
					m[yy-1][xx]=1;
				}
				xx=m[0].length-1-x;
				yy=m.length-1-y;
				if(m[yy][xx]==1) {
					m[yy+1][xx]=1;
					m[yy+1][xx-1]=1;
					m[yy+1][xx+1]=1;
					
					m[yy][xx+1]=1;
					m[yy][xx-1]=1;
					
					m[yy-1][xx+1]=1;
					m[yy-1][xx-1]=1;
					m[yy-1][xx]=1;
				}
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

	private List<Integer> xl = new ArrayList<Integer>(),
			yl = new ArrayList<Integer>();

	@Override
	public void mousePressed(MouseEvent e) {

		this.line.clear();
		this.pPre = e.getPoint();
		this.line.add(pPre);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point p = e.getPoint();
		this.line.add(p);
		this.lines.add(line);

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
		Point p = e.getPoint();
		Graphics g = this.getGraphics();
		g.drawLine(pPre.x, pPre.y, p.x, p.y);

		this.pPre = p;
		this.line.add(p);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("digit");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyCanvas ui = new MyCanvas(400, 400);
		jf.add(ui);
		jf.pack();
		jf.setVisible(true);
	}

}
