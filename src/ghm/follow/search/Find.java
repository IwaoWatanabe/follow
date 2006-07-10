package ghm.follow.search;

import ghm.follow.gui.FileFollowingPane;
import ghm.follow.gui.FollowApp;
import ghm.follow.gui.FollowAppAction;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class Find extends FollowAppAction {
	public static final String NAME = "find";
	private FindDialog dialog_;
	private JTextField find_;
	private JButton findButton_;
	private JButton clearButton_;
	private JButton closeButton_;

	public Find(FollowApp app) {
		super(app, app.getResourceBundle().getString("action.Find.name"), app.getResourceBundle()
				.getString("action.Find.mnemonic"), app.getResourceBundle().getString(
				"action.Find.accelerator"));
	}

	public void actionPerformed(ActionEvent e) {
		getApp().setCursor(Cursor.WAIT_CURSOR);
		if (dialog_ == null) {
			dialog_ = new FindDialog();
			dialog_.setLocationRelativeTo(getApp().getFrame());
			dialog_.setLocation(30, 30);
			dialog_.pack();
		}
		find_.grabFocus();
		find_.selectAll();
		dialog_.setVisible(true);
		getApp().setCursor(Cursor.DEFAULT_CURSOR);
	}

	private int doFind() {
		// get the current selected tab
		FileFollowingPane pane = getApp().getSelectedFileFollowingPane();
		// search the tab with the given text
		SearchableTextArea textArea = (SearchableTextArea) pane.getTextArea();
		textArea.selectAll();
		return textArea.highlight(find_.getText());
	}

	class FindDialog extends JDialog {
		FindDialog() {
			super(Find.this.getApp().getFrame(), Find.this.getApp().getResourceBundle().getString(
					"dialog.Find.title"), false);
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						setVisible(false);
					}
				}
			});
			setResizable(false);
			JComponent contentPane = (JComponent) getContentPane();
			contentPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

			JPanel findPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

			// add the find field & label
			findPanel.add(new JLabel(getApp().getResourceBundle().getString(
					"dialog.Find.findText.label")));
			find_ = new JTextField(15);
			find_.setHorizontalAlignment(JTextField.LEFT);
			findPanel.add(find_);
			// add the find button
			findButton_ = new JButton(getApp().getResourceBundle().getString(
					"dialog.Find.findButton.label"));
			findButton_.setMnemonic(getApp().getResourceBundle().getString(
					"dialog.Find.findButton.mnemonic").charAt(0));
			findButton_.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int found = doFind();
					if (found == 0) {
						JOptionPane.showMessageDialog(dialog_, "Search term not found.",
								"Search results", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			// add the clear button
			clearButton_ = new JButton(getApp().getResourceBundle().getString(
					"dialog.Find.clearButton.label"));
			clearButton_.setMnemonic(getApp().getResourceBundle().getString(
					"dialog.Find.clearButton.mnemonic").charAt(0));
			clearButton_.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// get the current selected tab
					FileFollowingPane pane = getApp().getSelectedFileFollowingPane();
					// clear the highlights from the searched tab
					SearchableTextArea textArea = (SearchableTextArea) pane.getTextArea();
					textArea.removeHighlights();
				}
			});
			// add the close button
			closeButton_ = new JButton(getApp().getResourceBundle().getString(
					"dialog.Find.closeButton.label"));
			closeButton_.setMnemonic(getApp().getResourceBundle().getString(
					"dialog.Find.closeButton.mnemonic").charAt(0));
			closeButton_.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog_.setVisible(false);
				}
			});
			// add the buttons to the dialog
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(findButton_);
			buttonPanel.add(clearButton_);
			buttonPanel.add(closeButton_);
			// add everything to the content pane
			contentPane.add(findPanel, BorderLayout.CENTER);
			contentPane.add(buttonPanel, BorderLayout.SOUTH);
		}

		/**
		 * Override method to add ESCAPE key action for window close
		 */
		protected JRootPane createRootPane() {
			KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
			JRootPane rootPane = new JRootPane();
			rootPane.registerKeyboardAction(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					closeButton_.doClick();
				}
			}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
			rootPane.registerKeyboardAction(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					findButton_.doClick();
				}
			}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			return rootPane;
		}
	}
}