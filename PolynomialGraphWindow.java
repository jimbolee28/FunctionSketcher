import java.awt.EventQueue;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class PolynomialGraphWindow {

	private JFrame frame;
	private JTextField txtCoeff;
	private double xi, yi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PolynomialGraphWindow window = new PolynomialGraphWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PolynomialGraphWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		JLabel lblEquation = new JLabel("Equation");
		lblEquation.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblEquation.setHorizontalAlignment(SwingConstants.CENTER);
		lblEquation.setBounds(646, 11, 116, 32);
		frame.getContentPane().add(lblEquation);

		JTextPane txtpnDescription = new JTextPane();
		txtpnDescription.setText("Enter the coefficients of the polynomial you wish to graph. "
				+ "Make sure to separate the coefficients with spaces. If the degree of the polynomial is n, "
				+ "please enter n+1 coefficients according to the format below."
				+ "\r\n\r\nn = 1 :  y = Ax + B                                "
				+ "(Enter: A B)\r\nn = 2 :  y = Ax^2 + Bx + C                  (Enter: A B C)"
				+ "            \r\nn = 3 :  y = Ax^3 + Bx^2 + Cx + D    (Enter: A B C D)\r\n" + "etc...");
		txtpnDescription.setBounds(546, 54, 314, 133);
		frame.getContentPane().add(txtpnDescription);

		JLabel lblCurrenteq = new JLabel("y = x");
		lblCurrenteq.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCurrenteq.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrenteq.setBounds(10, 11, 500, 27);
		frame.getContentPane().add(lblCurrenteq);

		// GRAPH

		JPanel graphPanel = new JPanel();
		graphPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		graphPanel.setBounds(10, 50, frame.getHeight() - 100, frame.getHeight() - 100);
		frame.getContentPane().add(graphPanel);
		graphPanel.setLayout(null);

		double[] list = new double[] { 1.0, 0.0 };
		PolynomialHandler w = new PolynomialHandler(50, 0.1, list);
		w.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xi = e.getX();
				yi = e.getY();
			}
		});
		w.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				double dx = e.getX() - xi, dy = e.getY() - yi;
				w.setxOff(w.getxOff() + dx / w.getScale());
				w.setyOff(w.getyOff() - dy / w.getScale());
				w.repaint();
				xi = e.getX();
				yi = e.getY();
			}
		});
		w.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					if (w.getScale() <= 10) {
						w.setScale(w.getScale());
						w.repaint();
					} else {
						w.setScale(10);
						w.repaint();
					}
				} else {
					if (w.getScale() >= 20) {
						w.setScale(-10);
						w.repaint();
					} else {
						w.setScale(-w.getScale() / 2);
						w.repaint();
					}
				}
			}
		});
		w.setBounds(0, 0, graphPanel.getWidth(), graphPanel.getHeight());
		graphPanel.add(w);

		txtCoeff = new JTextField();
		txtCoeff.setBounds(593, 198, 228, 32);
		frame.getContentPane().add(txtCoeff);
		txtCoeff.setColumns(10);

		JButton btnGraph = new JButton("Graph");
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double[] dblArr = Arrays.stream(txtCoeff.getText().toString().split(" ")).mapToDouble(Double::parseDouble)
						.toArray();
				System.out.println(Arrays.toString(dblArr));
				w.setCoeff(dblArr);
				lblCurrenteq.setText(w.eqStr());
				w.repaint();
			}
		});
		btnGraph.setBounds(665, 241, 89, 23);
		frame.getContentPane().add(btnGraph);

		JButton btnZoomIn = new JButton("+");
		btnZoomIn.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (w.getScale() <= 10) {
					w.setScale(w.getScale());
					w.repaint();
				} else {
					w.setScale(10);
					w.repaint();
				}
			}
		});
		btnZoomIn.setBounds(517, 451, 51, 43);
		frame.getContentPane().add(btnZoomIn);

		JButton btnZoomOut = new JButton("-");
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (w.getScale() >= 20) {
					w.setScale(-10);
					w.repaint();
				} else {
					w.setScale(-w.getScale() / 2);
					w.repaint();
				}
			}
		});
		btnZoomOut.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnZoomOut.setBounds(517, 493, 51, 43);
		frame.getContentPane().add(btnZoomOut);

		JButton btnUp = new JButton("^");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				w.setyOff(w.getyOff() - 1);
				w.repaint();
			}
		});
		btnUp.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnUp.setBounds(662, 404, 51, 43);
		frame.getContentPane().add(btnUp);

		JButton btnLeft = new JButton("<");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				w.setxOff(w.getxOff() + 1);
				w.repaint();
			}
		});
		btnLeft.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLeft.setBounds(610, 449, 51, 43);
		frame.getContentPane().add(btnLeft);

		JButton btnDown = new JButton("\\/");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				w.setyOff(w.getyOff() + 1);
				w.repaint();
			}
		});
		btnDown.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDown.setBounds(662, 493, 51, 43);
		frame.getContentPane().add(btnDown);

		JButton btnRight = new JButton(">");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				w.setxOff(w.getxOff() - 1);
				w.repaint();
			}
		});
		btnRight.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnRight.setBounds(713, 449, 51, 43);
		frame.getContentPane().add(btnRight);

	}
}
