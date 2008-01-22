/*
 * Copyright (C) 2000-2003 Greg Merrill (greghmerrill@yahoo.com)
 * 
 * This file is part of Follow (http://follow.sf.net).
 * 
 * Follow is free software; you can redistribute it and/or modify it under the
 * terms of version 2 of the GNU General Public License as published by the Free
 * Software Foundation.
 * 
 * Follow is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Follow; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */

package ghm.follow.gui;

import ghm.follow.FollowApp;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;

/**
 * Action which deletes the contents of all followed files.
 * 
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
public class DeleteAll extends FollowAppAction
{
	public static final String NAME = "deleteAll";
	private Logger log = LoggerFactory.getLogger(DeleteAll.class);

	public DeleteAll(FollowApp app) throws IOException
	{
		super(app, FollowApp.getResourceString("action.DeleteAll.name"),
				FollowApp.getResourceString("action.DeleteAll.mnemonic"),
				FollowApp.getResourceString("action.DeleteAll.accelerator"),
				FollowApp.getIcon(DeleteAll.class, "action.DeleteAll.icon"));
	}

	public void actionPerformed(ActionEvent e)
	{
		if (getApp().getAttributes().confirmDeleteAll())
		{
			DisableableConfirm confirm = new DisableableConfirm(getApp().getFrame(),
					FollowApp.getResourceString("dialog.confirmDeleteAll.title"),
					FollowApp.getResourceString("dialog.confirmDeleteAll.message"),
					FollowApp.getResourceString("dialog.confirmDeleteAll.confirmButtonText"),
					FollowApp.getResourceString("dialog.confirmDeleteAll.doNotConfirmButtonText"),
					FollowApp.getResourceString("dialog.confirmDeleteAll.disableText"));
			confirm.pack();
			confirm.setVisible(true);
			if (confirm.markedDisabled())
			{
				getApp().getAttributes().setConfirmDeleteAll(false);
			}
			if (confirm.markedConfirmed())
			{
				performDelete();
			}
		}
		else
		{
			performDelete();
		}
	}

	private void performDelete()
	{
		getApp().setCursor(Cursor.WAIT_CURSOR);
		List<FileFollowingPane> allFileFollowingPanes = getApp().getAllFileFollowingPanes();
		try
		{
			for (FileFollowingPane fileFollowingPane : allFileFollowingPanes)
			{
				fileFollowingPane.clear();
			}
			getApp().setCursor(Cursor.DEFAULT_CURSOR);
		}
		catch (IOException ioe)
		{
			log.error("IOException error in DeleteAll", ioe);
			getApp().setCursor(Cursor.DEFAULT_CURSOR);
			JOptionPane.showMessageDialog(getApp().getFrame(),
					FollowApp.getResourceString("message.unableToDeleteAll.text"),
					FollowApp.getResourceString("message.unableToDeleteAll.title"),
					JOptionPane.WARNING_MESSAGE);
		}
	}
}