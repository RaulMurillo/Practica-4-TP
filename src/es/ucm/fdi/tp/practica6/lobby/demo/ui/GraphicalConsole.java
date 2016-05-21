package es.ucm.fdi.tp.practica6.lobby.demo.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A graphical console
 */
public class GraphicalConsole extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 290434570786218277L;

	protected String name;

    private JScrollPane jspOutput;
    private JTextField jtInput;
    private JTextArea jtaOutput;

    /**
     * Creates a new graphical console
     */
    public GraphicalConsole(String name) {
        initComponents();
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Writes something in the output area. Automatically adds
     * a newline right after it.
     *
     * @param text to write; to generate intermediate newlines, use "\n".
     */
    public void showText(String text) {
        jtaOutput.append(text + "\n");
        // scrolls to display the last line written
        Point lastPoint = new Point(0,
                (int) jtaOutput.getSize().getHeight() - 1);
        jspOutput.getViewport().setViewPosition(lastPoint);
    }


    /**
     * Closes this window (and all others).
     */
    public void closeWindow() {
        System.exit(0);
    }

    /**
     * Process a text-event (text written + enter or clicked on "send")
     */
    private void processTextEvent() {
        String text = jtInput.getText();
        handleTextEntry(text);
        showText("> " + text);
        jtInput.setText("");
    }

    protected void handleTextEntry(String text) {
        // by default, do nothing
    }

    /**
     * this[BorderLayout]
     *           jtInput jbConfirm
     *           jspOutput
     *               jtaOutput
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        jtInput = new JTextField();
        JButton jbConfirm = new JButton();
        jspOutput = new JScrollPane();
        jtaOutput = new JTextArea();

        setLayout(new GridBagLayout());

        jtInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                processTextEvent();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(jtInput, gridBagConstraints);

        jbConfirm.setText("  !  ");
        jbConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                processTextEvent();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(jbConfirm, gridBagConstraints);

        jtaOutput.setColumns(20);
        jtaOutput.setRows(5);
        jtaOutput.setFont(new Font("monospaced", Font.PLAIN, 10));
        jtaOutput.setEditable(false);
        jspOutput.setViewportView(jtaOutput);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(jspOutput, gridBagConstraints);
    }
}
