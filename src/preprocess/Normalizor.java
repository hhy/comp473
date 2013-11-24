package proj.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import proj.ImgTool;
import proj.mnist.MatrixFile;
import proj.mnist.TrainingData;

public class Normalizor {
	int[][] pic;

	int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
	int wPic, hPic;

	public Normalizor(int[][] pic) {
		this.pic = pic;
		wPic = this.pic[0].length;
		hPic = this.pic.length;
		this.setObjectPos();
	}

	private boolean isZero(int[][] is, int x, int y) {
		if (x * y > 0)
			return false;
		if (x < 0) {
			for (int i = 0; i < wPic; i++) {
				if (is[y][i] > 0)
					return false;
			}
			return true;
		} else {
			for (int i = 0; i < hPic; i++) {
				if (is[i][x] > 0)
					return false;
			}
			return true;
		}

	}

	void setObjectPos() {

		for (int y = 0; y < hPic; y++) {
			if (y1 == -1) {
				if (!this.isZero(this.pic, -1, y))
					y1 = y;
			}
			if (y2 == -1) {
				if (!this.isZero(this.pic, -1, hPic - y - 1))
					y2 = hPic - y - 1;
			}
			if (y1 > 0 && y2 > 0)
				break;
		}
		for (int x = 0; x < wPic; x++) {
			if (x1 == -1) {
				if (!this.isZero(this.pic, x, -1))
					x1 = x;
			}
			if (x2 == -1) {
				if (!this.isZero(this.pic, wPic - x - 1, -1))
					x2 = wPic - x - 1;
			}
			if (x1 > 0 && x2 > 0)
				break;
		}
	}

	int[][] subMatrix(int[][] m, int xOffset, int w, int yOffset, int h){
		int[][] _img=new int[h][];
		for(int i=0; i<h; i++){
			_img[i]=new int[w];
			for(int j=0; j<w; j++){
				_img[i][j]=m[yOffset+i][xOffset+j];
			}
		}
		return _img;
	}
	int[][] scaleInnerObject(int w, int h) {
		int[][] obj=this.subMatrix(this.pic, x1, x2-x1+1, y1, y2-y1+1);

		double pw = ((double) w) / (this.x2-this.x1), ph = ((double) h)
				/ (this.y2-this.y1);

		int[][] p = new int[h][];
		for (int y = 0; y < h; y++) {
			p[y] = new int[w];
			for (int x = 0; x < w; x++) {
				int _x = (int) (x / pw), _y = (int) (y / ph);
				if (_y > obj.length || _x > obj[0].length) {
					System.out.println(String.format(
							"x: %d, y: %d, _x: %d, _y: %d, pw=%f, ph=%f", x, y,
							_x, _y, pw, ph));
				}
				p[y][x] = obj[_y][_x];
			}
		}

		return p;
	}
	
	public static int[][] normalize(int[][] m){
		Normalizor n = new Normalizor(m);
		return n.scaleInnerObject(28, 28);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("=================start==================");

		MatrixFile fMatrix = new MatrixFile(TrainingData.pathImageTraining);
		int[][] m = (int[][]) fMatrix.getObject(4);
		Normalizor n = new Normalizor(m);
		//System.out.println(n.getDimension());
		//n.scaleInnerObject(28, 28);
		//ImgTool.showImg(n.scaleInnerObject(28, 28), "scale", 400);
		//ImgTool.showSubImg(m, "", 400, n.x1, n.x2, n.y1, n.y2);
		

		System.out.println("===================end======================");

	}

	String getDimension() {
		return String.format("(Width: %d - %d, Height: %d - %d", this.x1,
				this.x2, this.y1, this.y2);
	}

}
