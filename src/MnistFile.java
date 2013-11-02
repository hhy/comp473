package proj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MnistFile extends FileInputStream {
	
	
	public MnistFile(String name) throws FileNotFoundException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	public void init() throws IOException{
		this.magicNumber=this.readInt();
		this.numObjects=this.readInt();
	}

	String path;
	int magicNumber;
	int numObjects;
	
	String info(){
		return String.format("Magic Number: %d, number of objects: %d", this.magicNumber, this.numObjects);
	}
	int readInt() throws IOException{
		byte[] bs=new byte[4];
		this.read(bs);
		return (((bs[0] & 0x0ff) * 256 + (bs[1] & 0x0ff)) * 256 + (bs[2] & 0x0ff))
				* 256 + (bs[3] & 0x0ff);
	}
	
}
