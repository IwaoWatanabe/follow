package ghm.follow.test.gui;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.Exit;
import ghm.follow.gui.FollowApp;

import java.io.File;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import junit.framework.TestCase;

public abstract class AppLaunchingTestCase extends TestCase {

  public AppLaunchingTestCase (String name) { super(name); }

  public void setUp () throws Exception {
    if (FollowAppAttributes.PROPERTY_FILE.exists()) {
      FollowAppAttributes.PROPERTY_FILE.delete();
    }
    FollowAppAttributes.PROPERTY_FILE.deleteOnExit();
    FollowApp.main(new String[0]);
    doPostLaunch();
  }

  protected void doPostLaunch () throws Exception {
    app_ = FollowApp.getInstance();
    systemInterface_ = new TestSystemInterface();
    app_.setSystemInterface(systemInterface_);
  }

  public void tearDown () throws Exception {
    invokeAction(app_.getAction(Exit.NAME));
    while (!systemInterface_.exitCalled()) { Thread.sleep(250); }
    if (!FollowAppAttributes.PROPERTY_FILE.delete()) {
		fail("Couldn't delete property file [" + FollowAppAttributes.PROPERTY_FILE.getAbsolutePath()
				+ "]");
    }
  }

  protected void invokeAndWait (Runnable runnable) {
    try { SwingUtilities.invokeAndWait(runnable); }
    catch (Exception e) { throw new RuntimeException(e.getMessage()); }
  }

  protected void invokeAction (final Action action) {
    invokeAndWait(new Runnable () { public void run () { action.actionPerformed(null); } });
  }

  protected File createTempFile () throws IOException {
    File file = File.createTempFile("followedFile", null);
    file.deleteOnExit();
    return file;
  }

  protected FollowApp app_;
  protected TestSystemInterface systemInterface_;
}

