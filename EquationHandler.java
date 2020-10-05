import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class EquationHandler extends JComponent {

	private static final long serialVersionUID = 1L;
	private double xSCALE;
	private double ySCALE;
	private double STEP;
	private double xOff = 0;
	private double yOff = 0;
	private String equation;
	private static ArrayList<Point> points;

	public EquationHandler(String equation, double xscale, double yscale, double step) {
		xSCALE = xscale;
		ySCALE = yscale;
		STEP = step;
		this.equation = equation;
		points = new ArrayList<Point>();
	}

	public void setEquation(String eq) {
		equation = eq;
		points.clear();
	}

	public String getEquation() {
		return equation;
	}

	public void setxScale(double scale) {
		xSCALE += scale;
	}

	public double getxScale() {
		return xSCALE;
	}

	public void setyScale(double scale) {
		ySCALE += scale;
	}

	public double getyScale() {
		return ySCALE;
	}

	public void setStep(double step) {
		STEP = step;
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

	public String reformatExponents(String eq) {
		eq = eq.replaceAll(" ", "");
		String operators = "+-*/()";
		while (eq.contains("^")) {
			int index = eq.indexOf("^") - 1, left = Integer.MAX_VALUE, right = -1;
			String base = "";
			if (eq.charAt(index) == ')') {
				while (eq.charAt(index) != '(')
					index--;
				base = eq.substring(index, eq.indexOf("^"));
				left = index;
			} else {
				while (index >= 0 && !operators.contains(eq.substring(index, index + 1)))
					index--;
				base = eq.substring(index + 1, eq.indexOf("^"));
				left = index + 1;
			}
			index = eq.indexOf("^") + 1;
			while (index < eq.length() && !operators.contains(eq.substring(index, index + 1)))
				index++;
			right = index;
			int exponent = Integer.parseInt(eq.substring(eq.indexOf("^") + 1, index));

			String result = "";
			for (int i = 0; i < exponent; i++) {
				if (i < exponent - 1)
					result += base + "*";
				else
					result += base;
			}
			eq = eq.substring(0, left) + "(" + result + ")" + eq.substring(right);
		}
		return eq;
	}

	public Double yVal(String eq, double x) throws NumberFormatException, ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String result = engine.eval(reformatExponents(eq).replaceAll("x", "" + Math.round(x * 10) / 10.0)).toString();
		// System.out.println(Math. round(x * 10) / 10.0 + ", " + result);
		if (result.equals("Infinity") || result.equals("-Infinity"))
			return null;
		return ySCALE * Double.parseDouble(result);
	}

	public void paintComponent(Graphics g) {

		// (x0, y0) is the origin
		int x0 = getWidth() / 2 + (int) (xOff * xSCALE);
		int y0 = getHeight() / 2 - (int) (yOff * ySCALE);

		// x and y axes
		Graphics2D axes = (Graphics2D) g;
		axes.setStroke(new BasicStroke(2));
		axes.setColor(Color.black);

		axes.drawLine(0, y0, 10000, y0); // x axis
		axes.drawLine(x0, 0, x0, 10000); // y axis

		// actual graph
		Graphics2D graph = (Graphics2D) g;
		graph.setStroke(new BasicStroke(2));
		graph.setColor(Color.red);

		Path2D path = new Path2D.Double();
		boolean pathEmpty = true, pointsEmpty = points.isEmpty();
		int counter = 0;
		for (double x = -10; x <= 10; x += STEP) {
			if (pointsEmpty) {
				Double yValue = null;
				try {
					yValue = yVal(equation, x);
				} catch (NumberFormatException | ScriptException e1) {
					e1.printStackTrace();
				}
				points.add(new Point(x, yValue));
			}
			if (points.get(counter).y == null) {
				graph.draw(path);
				path.reset();
				pathEmpty = true;
			} else {
				if (pathEmpty)
					path.moveTo(x0 + xSCALE * points.get(counter).x, y0 - points.get(counter).y);
				else
					path.lineTo(x0 + xSCALE * points.get(counter).x, y0 - points.get(counter).y);
				pathEmpty = false;
			}
			counter++;
		}
		graph.draw(path);
	}

}

class Point {

	Double x, y;

	public Point(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

}
