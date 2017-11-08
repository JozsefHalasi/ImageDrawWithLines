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
- load an image (source image)
- get a list of all the colors in the source image
- create 2 blank images (image1 and image2)
- draw a random polygon or circle on image1 using a random color from source image
- compare image1 to the source image
- if it's closer in color to the source image than image2, copy image1 to image2; if not, copy image2 to image1 and continue drawing more random shapes and comparing
- post the results and bits of code
*/
public class ImageQuantization{
	
	public static void main(String args[]){
	
		BufferedImage img = null;
        int[][] simpleImage;
		
		try{
			img = ImageIO.read(new File("penguin.jpg"));
            
			JFrame window = new JFrame();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setBounds(400, 400, img.getWidth(), img.getHeight());

			Set<Integer> set = new TreeSet<Integer>();
            simpleImage = new int[img.getWidth()][img.getHeight()];
			for(int i = 0; i < img.getWidth(); i++){
				for(int j = 0; j < img.getHeight(); j++){
					set.add(img.getRGB(i,j));
                    simpleImage[i][j]= img.getRGB(i,j);
				}
			}
			
			myWindow w = new myWindow(img, set, simpleImage);
			window.getContentPane().add(w);
			window.setVisible(true);
			while(true){
				w.drawLine();
				window.invalidate();
				window.repaint();
			}
        }catch(IOException e){
            e.printStackTrace();
        }
	}
}

class myWindow extends JPanel{
	
    public BufferedImage img;
    public int[][] eredeti;
    public int[][] kep1;
    public int[][] kep2;
	public Set<Integer> map;
	public int imgHeight;
	public int imgWidth;
	public int mapSize;

	public myWindow(BufferedImage valami, Set<Integer> map, int[][] tomb){
		super();
		this.img = createBufferedImage(valami);
		this.map = map;
		this.imgHeight = valami.getHeight();
		this.imgWidth = valami.getWidth();
		this.mapSize = map.size();
        this.kep1 = new int[imgWidth][imgHeight];
        this.kep2 = new int[imgWidth][imgHeight];
        this.eredeti = new int[imgWidth][imgHeight];
		
		for(int i = 0; i < imgWidth; i++){
            for(int j = 0; j < imgHeight; j++){
                Color c = new Color(255,255,255);
                this.eredeti[i][j] = img.getRGB(i,j);
                this.kep1[i][j] = c.getRGB();
                this.kep2[i][j] = c.getRGB();
            }
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        for(int i = 0; i < imgWidth; i++){
            for(int j = 0; j < imgHeight; j++){
                img.setRGB(i,j,kep1[i][j]);
                
            }
        }
		g.drawImage(img, 0, 0, null);
	}
	
	public int getRandomColor(){
		Random rn = new Random();
		java.util.List<Integer> ujLista = new ArrayList<Integer>(map);
		return ujLista.get(rn.nextInt(mapSize));
	}
	
	public int tavolsag(int[][] a, int[][] b){
		int Sum = 0;
        //System.out.println(a.toString());
       // System.out.println(b.toString());
		for(int i = 0; i < imgWidth; i++){
				for(int j = 0; j < imgHeight; j++){
					Color aC = new Color(a[i][j]);
                    Color bC = new Color(b[i][j]);


					Sum+=Math.sqrt(
						((aC.getRed() - bC.getRed())*(aC.getRed() - bC.getRed()))+
						((aC.getGreen() - bC.getGreen())*(aC.getGreen() - bC.getGreen()))+
						((aC.getBlue() - bC.getBlue())*(aC.getBlue() - bC.getBlue())));
						
				}
			}

		return Sum;
	}

	public BufferedImage createBufferedImage(BufferedImage image) {
          ColorModel cm = image.getColorModel();
          boolean premultiplied = cm.isAlphaPremultiplied();
          WritableRaster raster = image.copyData(image.getRaster());
          return new BufferedImage(cm, raster, premultiplied, null);
      }
	
	public void drawLine(){
		Random rn = new Random();
		int length = 13;
		double x = rn.nextInt(img.getWidth());
		double y = rn.nextInt(img.getHeight());
		double meredek = rn.nextDouble();
		int negyed = rn.nextInt(7);
		int color = getRandomColor();
		
		switch(negyed){
			case 0:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					y += meredek;
					x += 1;
				}
				break;
			case 1:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					x += meredek;
					y += 1;
				}
				break;
			case 2:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					y -= meredek;
					x += 1;
				}
				break;				
			case 3:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					x -= meredek;
					y += 1;
				}
				break;
			case 4:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					y += meredek;
					x -= 1;
				}
				break;				
			case 5:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					x += meredek;
					y -= 1;
				}
				break;
			case 6:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					y -= meredek;
					x -= 1;
				}
				break;
			default:
				for(int i = 0; i < length; i++){
					if(y > imgHeight-1 || y < 0 || x > imgWidth-1 || x < 0) break;
					kep1[(int)x][(int)y] = color;
					x -= meredek;
					y -= 1;
				}
				break;
		}
        //System.out.println(tavolsag(eredeti, kep1));
        //System.out.println(tavolsag(eredeti, kep2));
		if(tavolsag(eredeti, kep1) > tavolsag(eredeti, kep2)){
            //System.out.println("img rossz");
            for(int i = 0; i < imgWidth; i++){
                for(int j = 0; j < imgHeight; j++){
                    kep1[i][j] = kep2[i][j];
                    
                }
            }
		}else{
            //System.out.println("img jo");
            for(int i = 0; i < imgWidth; i++){
                for(int j = 0; j < imgHeight; j++){
                    kep2[i][j] = kep1[i][j];
                    
                }
            }
		}
	}
}
