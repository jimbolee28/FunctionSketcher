import java.awt.EventQueue;

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
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class EquationGraphWindow {

	private JFrame frame;
	private JTextField txtCoeff;
	private double xi, yi;

	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EquationGraphWindow window = new EquationGraphWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application.
	public EquationGraphWindow() {
		initialize();
	}

	// Initialize the contents of the frame
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		JLabel lblEquation = new JLabel("Equation");
		lblEquation.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblEquation.setHorizontalAlignment(SwingConstants.CENTER);
		lblEquation.setBounds(646, 11, 116, 32);
		frame.getContentPane().add(lblEquation);

		JTextPane txtpnDescription = new JTextPane();
		txtpnDescription.setText("Enter an equation. Be explicit with multiplication by putting "
				+ "an asterisk for all multiplication (no parenthetical multiplication). "
				+ "Fractional or variable exponents are not supported. Asymptotes may be glitchy.");
		txtpnDescription.setBounds(546, 54, 314, 83);
		frame.getContentPane().add(txtpnDescription);

		JLabel lblCurrenteq = new JLabel("y = x^2");
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

		EquationHandler w = new EquationHandler("x^2", 50, 50, 0.1);
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
				w.setxOff(w.getxOff() + dx / w.getxScale());
				w.setyOff(w.getyOff() - dy / w.getyScale());
				w.repaint();
				xi = e.getX();
				yi = e.getY();
			}
		});
		w.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					if (w.getxScale() <= 10 && w.getyScale() <= 10) {
						w.setxScale(w.getxScale());
						w.setyScale(w.getyScale());
						w.repaint();
					} else {
						w.setxScale(10);
						w.setyScale(10);
						w.repaint();
					}
				} else {
					if (w.getxScale() >= 20 && w.getyScale() >= 20) {
						w.setxScale(-10);
						w.setyScale(-10);
						w.repaint();
					} else {
						w.setxScale(-w.getxScale() / 2);
						w.setyScale(-w.getyScale() / 2);
						w.repaint();
					}
				}
			}
		});
		w.setBounds(0, 0, graphPanel.getWidth(), graphPanel.getHeight());
		graphPanel.add(w);

		txtCoeff = new JTextField();
		txtCoeff.setBounds(579, 148, 228, 32);
		frame.getContentPane().add(txtCoeff);
		txtCoeff.setColumns(10);

		JButton btnGraph = new JButton("Graph");
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				w.setEquation(txtCoeff.getText());
				lblCurrenteq.setText("y = " + txtCoeff.getText());
				w.repaint();
			}
		});
		btnGraph.setBounds(662, 191, 89, 23);
		frame.getContentPane().add(btnGraph);

		JButton btnZoomIn = new JButton("+");
		btnZoomIn.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (w.getxScale() <= 10 && w.getyScale() <= 10) {
					w.setxScale(w.getxScale());
					w.setyScale(w.getyScale());
					w.repaint();
				} else {
					w.setxScale(10);
					w.setyScale(10);
					w.repaint();
				}
			}
		});
		btnZoomIn.setBounds(517, 451, 51, 43);
		frame.getContentPane().add(btnZoomIn);

		JButton btnZoomOut = new JButton("-");
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (w.getxScale() >= 20 && w.getyScale() >= 20) {
					w.setxScale(-10);
					w.setyScale(-10);
					w.repaint();
				} else {
					w.setxScale(-w.getxScale() / 2);
					w.setyScale(-w.getyScale() / 2);
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

		JButton btnDown = new JButton("V");
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
