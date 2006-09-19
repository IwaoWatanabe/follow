package ghm.follow.search;

import java.util.ArrayList;

/**
 * Implementation of a case sensitive search.  Nothing fancy.
 * 
 * @author chall
 */
public class CaseInsensitiveSearchStrategy extends SearchStrategy {

  public CaseInsensitiveSearchStrategy(String text) {
    super(text.toLowerCase());
  }

  public Result[] search(String term) {
    ArrayList results = new ArrayList();
    if (term != null && term.length() > 0 && text != null && text.length() > 0) {
      int pos = 0;
      while ((pos = text.indexOf(term.toLowerCase(), pos)) > -1) {
        results.add(new Result(pos, pos+term.length(), term));
        pos += term.length();
      }
    }
    Result[] retval = new Result[0];
    if (results.size() > 0) {
      retval = (Result[]) results.toArray(new Result[results.size()]);
    }
    return retval;
  }
}