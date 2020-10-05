import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.JComponent;

public class PolynomialHandler extends JComponent {

	private static final long serialVersionUID = 1L;
	private double SCALE;
	private double STEP;
	private double xOff = 0;
	private double yOff = 0;
	private double[] coeff;

	public PolynomialHandler(double scale, double step, double[] coeff) {
		SCALE = scale;
		STEP = step;
		this.coeff = coeff;
	}

	public void setScale(double scale) {
		SCALE += scale;
	}

	public double getScale() {
		return SCALE;
	}

	public void setStep(double step) {
		STEP = step;
	}

	public void setCoeff(double[] coeff) {
		this.coeff = coeff;
	}

	public double getxOff() {
		return xOff;
	}

	public void setxOff(double xOff) {
		this.xOff = xOff;
	}

	public double getyOff() {
		return yOff;
	}

	public void setyOff(double yOff) {
		this.yOff = yOff;
	}

	public String term(boolean isFirstTerm, double c, int p) {
		String sign = (c > 0) ? " + " : " - ";
		String coeffStr = (c % 1.0 == 0.0) ? "" + (int) Math.abs(c) : "" + Math.abs(c);
		String coeffStrFirstTerm = (c % 1 == 0) ? "" + (int) (c) : "" + c;
		String end = (p == 1) ? "x" : ((p == 0) ? "" : "x^" + p);
		if (isFirstTerm) {
			if (Math.abs(c) == 1.0 && p > 0)
				return end;
			else
				return coeffStrFirstTerm + end;
		}
		if (Math.abs(c) == 1.0 && p > 0)
			return sign + end;
		return sign + coeffStr + end;
	}

	public String eqStr() {
		int currPower = coeff.length - 1;
		String result = "y = ";
		while (currPower >= 0) {
			if (currPower == coeff.length - 1)
				result += term(true, coeff[coeff.length - 1 - currPower], currPower);
			else if (coeff[coeff.length - 1 - currPower] != 0)
				result += term(false, coeff[coeff.length - 1 - currPower], currPower);
			currPower--;
		}
		return result;
	}

	public double yVal(double x) {
		double sum = 0;
		for (int i = 0; i < coeff.length; i++)
			sum += coeff[i] * Math.pow(x, coeff.length - 1 - i);
		return SCALE * sum;
	}

	public void paintComponent(Graphics g) {

		// (x0, y0) is the origin
		int x0 = getWidth() / 2 + (int) (xOff * SCALE);
		int y0 = getHeight() / 2 - (int) (yOff * SCALE);

		// x and y axes
		Graphics2D axes = (Graphics2D) g;
		axes.setStroke(new BasicStroke(2));
		axes.setColor(Color.black);

		axes.drawLine(0, y0, 10000, y0); // x axis
		axes.drawLine(x0, 0, x0, 10000); // y axis
		// axes.drawLine(x0 + SCALE, y0 - 5, x0 + SCALE, y0 + 5); tick marks (may need
		// Path2D)

		// actual graph
		Graphics2D graph = (Graphics2D) g;
		graph.setStroke(new BasicStroke(2));
		graph.setColor(Color.red);

		Path2D path = new Path2D.Double();
		int move = 0;
		for (double x = -10; x <= 10; x += STEP) {

			if (move == 0) {
				path.moveTo(x0 + SCALE * x, y0 - yVal(x));
				move++;
			} else {
				path.lineTo(x0 + SCALE * x, y0 - yVal(x));

			}
		}
		graph.draw(path);
	}

}