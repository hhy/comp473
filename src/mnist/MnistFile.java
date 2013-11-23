package proj.mnist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

public abstract class MnistFile extends FileInputStream {
	
	
	public MnistFile(String name) throws IOException {
		super(name);
		this.init();
	}

	
	private void init() throws IOException{
		this.magicNumber=this.readInt();
		this.numObjects=this.readInt();
	}

	String path;
	int magicNumber;
	public int numObjects;
	
	abstract int getObjectStoreSize();
	abstract int getOffsetObjectArrays();
	
	public Object getObject(int index) throws IOException{
		FileChannel fc=this.getChannel();
		
		
		fc.position(this.getOffsetObjectArrays()+index*this.getObjectStoreSize());
		return this.getObjectFromStream();
	}
	
	abstract Object getObjectFromStream() throws IOException;
	
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
