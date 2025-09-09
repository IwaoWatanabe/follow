package ghm.follow.jfc.ui;

import javax.swing.text.JTextComponent;
import ghm.follow.SearchEngine;

/**
 * An {@link OutputDestination} that filters what is shown.
 * 
 * @author Carl Hall (carl.hall@gmail.com)
 */
public class FilteredDestination extends JTextComponentDestination {
	private String filterTerm;

	public FilteredDestination(JTextComponent jTextArea, SearchEngine se,
			String filterTerm, boolean autoPositionCaret) {
		super(jTextArea, autoPositionCaret);
		this.filterTerm = filterTerm;
	}

	public void print(String s) {
		// TODO print only if s contains filterTerm
		super.print(s);
	}
}