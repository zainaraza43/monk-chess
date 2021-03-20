package CodeNet4By4;

// OverlayCanvas.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
   The 3D Canvas includes a status line,
   displayed in red, at the top left corner.

   Current status information is obtained from
   the NetFourByFour object each time postSwap() is called.
*/

import java.awt.*;
import org.jogamp.java3d.Canvas3D;

public class OverlayCanvas extends Canvas3D {

	private static final long serialVersionUID = 1L;
	private final static int XPOS = 15;
	private final static int YPOS = 25;
	private final static Font MSGFONT = new Font("SansSerif", Font.BOLD, 28);

	private NetFourByFour fbf;
	private String status;

	public OverlayCanvas(GraphicsConfiguration config, NetFourByFour fbf) {
		super(config);
		this.fbf = fbf;
	}
	
	/* Called by the rendering loop after completing all rendering to the canvas.
	 */
	public void postSwap() {
		Graphics2D g = (Graphics2D) getGraphics();
		g.setColor(Color.red);
		g.setFont(MSGFONT);

		if ((status = fbf.getStatus()) != null) // it has a value
			g.drawString(status, XPOS, YPOS);

		// this call is made to compensate for the javaw repaint bug, ...
		Toolkit.getDefaultToolkit().sync();
	} 

	// a function overriding repaint() to make the worst flickering disappear
	public void repaint() {
		Graphics2D g = (Graphics2D) getGraphics();
		paint(g);
	}

	// a function overriding paint() to compensate for the javaw repaint bug
	public void paint(Graphics g) {
		super.paint(g);
		Toolkit.getDefaultToolkit().sync();
	}
}