import java.awt.image.*;
import java.awt.*;
import java.awt.Graphics.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
* @author Jozsef Halasi
*
* @param source photo name
* @param output photo name
* @param iteration limit
*
* - load an image (source image)
* - get a list of all the colors in the source image
* - create 1 new blank image
* - draw a random polygon or circle on image using a random color from source image
* - compare image to the source image
* - if it's closer in color to the source image than image2, copy image1 to image2; if not, copy image2 to image1 and continue drawing more random shapes and comparing
*/
public class ImageDrawWithLines{
	
	public static void main(String args[]){
	
		BufferedImage img = null;

        final String output;
        final int limit;
        
		try{
            if(args.length == 0){
                System.out.println("No input arguments!");
                return;
            }
            
            img = ImageIO.read(new File(args[0]));
            output = args[1];
            limit = Integer.parseInt(args[2]);

			JFrame window = new JFrame();
			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			window.setBounds(0, 0, img.getWidth(), img.getHeight());

			Set<Integer> set = new TreeSet<Integer>();

			for(int i = 0; i < img.getWidth(); i++){
				for(int j = 0; j < img.getHeight(); j++){
					set.add(img.getRGB(i,j));
				}
			}
			
			myWindow w = new myWindow(img, set);
            w.setSize(img.getWidth(), img.getHeight());
			window.getContentPane().add(w);
			window.setVisible(true);
            
            int counter = 0;
            long startTime = System.currentTimeMillis();
            print("Start time: " + new Date().toString());
            
			while(counter < limit){
                counter++;
				w.drawCircle();
				//w.drawLine();
				window.invalidate();
				window.repaint();
                if(counter % 10000 == 0) {
                    long now = System.currentTimeMillis();
                    double timeSpent10k = (now - startTime) / 1000;
                    print("Seconds spent 10k: " + timeSpent10k);
                    ImageIO.write(w.drawImage, "jpg", new File(output));
                }
			}
            ImageIO.write(w.drawImage, "jpg", new File(output));
            
            long endTime = System.currentTimeMillis();
            double timeSpent = (endTime - startTime) / 60000;
            print("Total minutes spent: " + timeSpent);
            window.dispose();
            return;
            
        }catch(IOException e){
            e.printStackTrace();
        }
	}
    public static void print(String text){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));
            writer.append(text + System.getProperty("line.separator"));
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

class myWindow extends JPanel{
	
    public BufferedImage originalImage;
    public BufferedImage drawImage;
	
    public Set<Integer> map;
	public int imgHeight;
	public int imgWidth;
	public int mapSize;

	public myWindow(BufferedImage img, Set<Integer> map){
		super();
        this.originalImage = img;
		this.drawImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.map = map;
		this.imgHeight = img.getHeight();
		this.imgWidth = img.getWidth();
		this.mapSize = map.size();
        
		for(int i = 0; i < imgWidth; i++){
            for(int j = 0; j < imgHeight; j++){
                Color c = new Color(255,255,255);
                this.drawImage.setRGB(i, j, c.getRGB());
            }
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        g.drawImage(drawImage, 0, 0, null);
	}
	
	public int getRandomColor(){
		Random rn = new Random();
		java.util.List<Integer> ujLista = new ArrayList<Integer>(map);
		return ujLista.get(rn.nextInt(mapSize));
	}
    
    public double tavolsag(int a, int b){
        Color aC = new Color(a);
        Color bC = new Color(b);
        
        return Math.sqrt(((aC.getRed() - bC.getRed())*(aC.getRed() - bC.getRed()))+
                         ((aC.getGreen() - bC.getGreen())*(aC.getGreen() - bC.getGreen()))+
                         ((aC.getBlue() - bC.getBlue())*(aC.getBlue() - bC.getBlue())));
    }

	public BufferedImage createBufferedImage(BufferedImage image) {
          ColorModel cm = image.getColorModel();
          boolean premultiplied = cm.isAlphaPremultiplied();
          WritableRaster raster = image.copyData(image.getRaster());
          return new BufferedImage(cm, raster, premultiplied, null);
    }
	
	public void drawCircle(){
		Random rn = new Random();
		int length = 40;
		double x = rn.nextInt(imgWidth);
		double y = rn.nextInt(imgHeight);
		//double meredek = rn.nextDouble();
		//int negyed = rn.nextInt(7);
		int color = getRandomColor();
		
        int sumOld = 0;
        int sumUj = 0;		
		
					int korX = length;
					int korY = 0;
					int r = length;
				for(int i = length; i >= 0; i-- , korX--){
					//if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					

					
					
					korY = (int)Math.sqrt(r*r-korX*korX);
					if(korY > korX) break;
					if((int)(y+korY) > imgHeight-1 || (int)(y+korY) < 0 || (int)(x+korX) > imgWidth-1 || (int)(x+korX) < 0) break;
					drawImage.setRGB((int)(x+korX), (int)(y+korY), color);
					if((int)(x+korX) > imgHeight-1 || (int)(x+korX) < 0 || (int)(y+korY) > imgWidth-1 || (int)(y+korY) < 0) break;
					drawImage.setRGB((int)(y+korY), (int)(x+korX), color);
					
					if((int)(y-korY) > imgHeight-1 || (int)(y-korY) < 0 || (int)(x+korX) > imgWidth-1 || (int)(x+korX) < 0) break;
					drawImage.setRGB((int)(x+korX), (int)(y-korY), color);
					if((int)(x-korX) > imgHeight-1 || (int)(x-korX) < 0 || (int)(y+korY) > imgWidth-1 || (int)(y+korY) < 0) break;
					drawImage.setRGB((int)(y+korY), (int)(x-korX), color);
					
					
					if((int)(y-korY) > imgHeight-1 || (int)(y-korY) < 0 || (int)(x+korX) > imgWidth-1 || (int)(x+korX) < 0) break;
					drawImage.setRGB((int)(x-korX), (int)(y+korY), color);
					if((int)(x-korX) > imgHeight-1 || (int)(x-korX) < 0 || (int)(y+korY) > imgWidth-1 || (int)(y+korY) < 0) break;
					drawImage.setRGB((int)(y-korY), (int)(x+korX), color);
					
					if((int)(y-korY) > imgHeight-1 || (int)(y-korY) < 0 || (int)(x-korX) > imgWidth-1 || (int)(x-korX) < 0) break;
					drawImage.setRGB((int)(x-korX), (int)(y-korY), color);
					if((int)(x-korX) > imgHeight-1 || (int)(x-korX) < 0 || (int)(y-korY) > imgWidth-1 || (int)(y-korY) < 0) break;
					drawImage.setRGB((int)(y-korY), (int)(x-korX), color);					
				}

		
		
	}
	
	public void drawLine(){
		Random rn = new Random();
		int length = 5;
		double x = rn.nextInt(imgWidth);
		double y = rn.nextInt(imgHeight);
		double meredek = rn.nextDouble();
		int negyed = rn.nextInt(7);
		int color = getRandomColor();
		
        int sumOld = 0;
        int sumUj = 0;
        
		switch(negyed){
			case 0:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					y += meredek;
					x += 1;
				}
				break;
			case 1:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					x += meredek;
					y += 1;
				}
				break;
			case 2:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					y -= meredek;
					x += 1;
				}
				break;				
			case 3:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					x -= meredek;
					y += 1;
				}
				break;
			case 4:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					y += meredek;
					x -= 1;
				}
				break;				
			case 5:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					x += meredek;
					y -= 1;
				}
				break;
			case 6:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					y -= meredek;
					x -= 1;
				}
				break;
			default:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    sumUj+= tavolsag(color, originalImage.getRGB((int)x, (int)y));
                    sumOld+= tavolsag(drawImage.getRGB((int)x, (int)y), originalImage.getRGB((int)x, (int)y));
					x -= meredek;
					y -= 1;
				}
				break;
		}
        
        if(sumUj > sumOld) return;
        
        switch(negyed){
            case 0:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    y += meredek;
                    x += 1;
                }
                break;
            case 1:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    x += meredek;
                    y += 1;
                }
                break;
            case 2:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    y -= meredek;
                    x += 1;
                }
                break;
            case 3:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    x -= meredek;
                    y += 1;
                }
                break;
            case 4:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    y += meredek;
                    x -= 1;
                }
                break;
            case 5:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    x += meredek;
                    y -= 1;
                }
                break;
            case 6:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    y -= meredek;
                    x -= 1;
                }
                break;
            default:
                for(int i = 0; i < length; i++){
                    if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
                    drawImage.setRGB((int)x, (int)y, color);
                    x -= meredek;
                    y -= 1;
                }
                break;
        }
	}
}
