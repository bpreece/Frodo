
package com.bpreece.lotr;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Line-Oriented Text Reformatter (LOTR)
 *
 * Navigation and editing is based on the <em>current line</em> and the
 * <em>range</em>
 *
 * @author Ly
 */
public class LOTR
{

    /**
     * Attempts to match the given line to the given pattern.  If
     * the line matches, the method extracts the capture groups and returns
     * them as an array.  If the pattern does not match, the method returns
     * <code>null</code>.
     *
     * @param pattern
     * @param line
     * @return <code>true</code> if the current line matched the given pattern,
     * and <code>false</code> otherwise.
     */
    public static Object[] getCaptureGroups(Pattern pattern, String line)
    {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            int groupCount = matcher.groupCount();
            Object[] groupArray = new Object[groupCount + 1];
            for (int i = 0; i <= groupCount; i++) {
                groupArray[i] = matcher.group(i);
            }
            return groupArray;
        } else {
            return null;
        }
    }

    private ArrayList<String> lines;
    /*
     * The following constraints must always hold:
     *
     * 1) lineIndex >= 0
     * 2) lineIndex <= rangeIndex
     * 3) rangeIndex <= lines.size
     *
     * It follows that when lines is empty, then lineIndex == rangeIndex == 0.
     */
    private int lineIndex;
    private int rangeIndex;
    private Object[] groups = new String[0];

    /**
     * Construct a new instance of <code>LOTR</code>.to process the given
     * input text.  The instance makes a copy of the input list, so that
     * there is no conflict between this instance and the calling objects.
     *
     * The current line is set to the first line; that is, the line index is
     * set to index zero.The range is set to the entire list of input lines;
     * that is, the rangeIndex is set to the length of the list, or
     * equivalently, one more than the index of the last line.
     *
     * This class is not thread-safe.
     *
     * @param lines the input text to be processes
     */
    public LOTR(List<String> lines)
    {
        this.lines = new ArrayList<String>(lines);
        lineIndex = 0;
        rangeIndex = this.lines.size();
    }

    public int compareTo(String line)
    {
        return lines.get(lineIndex).compareTo(line);
    }

    public char charAt(int index)
    {
        return getLine().charAt(index);
    }

    public int indexOf(char c)
    {
        return lines.get(lineIndex).indexOf(c);
    }

    public int indexOf(String substring)
    {
        return lines.get(lineIndex).indexOf(substring);
    }

    public int indexOf(String substring, int fromIndex)
    {
        return lines.get(lineIndex).indexOf(substring, fromIndex);
    }

    public int indexOf(char c, int fromIndex)
    {
        return lines.get(lineIndex).indexOf(c, fromIndex);
    }

    public int lastIndexOf(char c)
    {
        return lines.get(lineIndex).lastIndexOf(c);
    }

    public int lastIndexOf(String substring)
    {
        return lines.get(lineIndex).lastIndexOf(substring);
    }

    public int lastIndexOf(String substring, int fromIndex)
    {
        return lines.get(lineIndex).lastIndexOf(substring, fromIndex);
    }

    public int lastIndexOf(char c, int fromIndex)
    {
        return lines.get(lineIndex).lastIndexOf(c, fromIndex);
    }

    public int length()
    {
        return lines.get(lineIndex).length();
    }

    /**
     * Return a copy of the object text in its current state.  This list will
     * reflect and changes which were made since the input text was passed to
     * the constructor.
     *
     * @return a copy of the object text in its current state.
     */
    public List<String> getLines()
    {
        return new ArrayList<String>(lines);
    }

    /**
     * Return the number of lines currently contained in the object text
     * being processed.
     *
     * @return the number of lines currently contained in the object text
     */
    public int getLineCount()
    {
        return lines.size();
    }

    /**
     * Return the index of the current line.
     *
     * @return the index of the current line.
     */
    public int getLineIndex()
    {
        return lineIndex;
    }

    /**
     * Set the current line to be the line with the given index.  If the line
     * index is set the same as the range index, then the current range will
     * be empty.
     *
     * This method will fail if the give line index is less than zero, or
     * if it is greater than the current range index.
     *
     * @param newIndex the index of the new line to be the new current line
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.
     */
    public boolean setLineIndex(int newIndex)
    {
        if (newIndex < 0 || newIndex > rangeIndex) {
            return false;
        } else {
            lineIndex = newIndex;
            return true;
        }
    }

    /**
     * Return the current line.  
     * 
     * This method throws an IndexOutOfBoundsException if the current range is
     * empty; that is, if the range index is the same as the current line 
     * method throws an IndexOutOfBoundsException
     * 
     * @return the current line
     * @throws IndexOutOfBoundsException if the current range empty
     */
    public String getLine()
            throws IndexOutOfBoundsException
    {
        return getNextLine(0);
    }

    /**
     * Return the line currently at the give index.  This method is not
     * restricted to the current range, but the index must be valid;  that is,
     * it must be at least zero, but strictly less than the number of lines in
     * the object text.
     * 
     * @param index the index of the line to return
     * @return the line currently at the give index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public String getLine(int index)
            throws IndexOutOfBoundsException
    {
        return lines.get(index);
    }

    /**
     * Return the line before the current line.
     * 
     * This method throws an IndexOutOfBoundsException if the current line is
     * already the first line of the object text.
     * 
     * @return the line before the current line.
     * @throws IndexOutOfBoundsException if the current line is at the first
     *         line
     */
    public String getPrevLine()
            throws IndexOutOfBoundsException
    {
        return getNextLine(-1);
    }

    /**
     * Return the line at the index which is the distance <code>n</code> 
     * before the current line.  The parameter <code>n</code> may be negative,
     * but the requested line must be valid, and may not be outside the current 
     * range;  that is, the index of the requested line may not be negative, 
     * and may not be greater than or equal the range index.
     * 
     * This method throws an IndexOutOfBoundsException if the index of the 
     * requested line is less than zero, or greater than or equal to the
     * current range index.
     * 
     * @param n the number of lines after the current line to return;
     * @return the line at the index which is the distance <code>n</code> 
     *         after the current line.
     * @throws IndexOutOfBoundsException if the request line exceeds the
     *         current range
     */
    public String getPrevLine(int n)
            throws IndexOutOfBoundsException
    {
        return getNextLine(-n);
    }

    /**
     * Return the line after the current line.
     *
     * This method throws an IndexOutOfBoundsException if the current range is
     * empty, or if the current line is already the last line in the range;
     * that is, the index of the requested line must be less than the range
     * index.
     *
     * @return the line after the current line.
     * @throws IndexOutOfBoundsException if the current line is already the
     *         last line in the range
     */
    public String getNextLine()
            throws IndexOutOfBoundsException
    {
        return getNextLine(1);
    }

    /**
     * Return the line at the index which is the distance <code>n</code>
     * after the current line.  The parameter <code>n</code> may be negative,
     * but the requested line must be valid, and may not be outside the current
     * range;  that is, the index of the requested line may not be negative,
     * and may not be greater than or equal the range index.
     *
     * This method throws an IndexOutOfBoundsException if the index of the
     * requested line is less than zero, or greater than or equal to the
     * current range index.
     *
     * @param n the number of lines after the current line to return;
     * @return the line at the index which is the distance <code>n</code>
     *         after the current line.
     * @throws IndexOutOfBoundsException if the request line exceeds the
     *         current range
     */
    public String getNextLine(int n)
            throws IndexOutOfBoundsException
    {
        int index = lineIndex + n;
        if (index < 0 || index >= rangeIndex) {
            throw new IndexOutOfBoundsException("index=" + index);
        } else {
            return lines.get(lineIndex);
        }
    }

    /**
     * Deletes and returns the line at index <code>n</code>.
     * @param n the index of the line to return
     * @return the line which was removed at index
     * @throws IndexOutOfBoundsException if the index <code>n</code> is
     *         less than zero or greater than the number of lines.
     */
    public String deleteLine(int n)
            throws IndexOutOfBoundsException
    {
        String line = lines.get(n);
        lines.remove(n);
        if (n < lineIndex) {
            lineIndex--;
        }
        if (n < rangeIndex) {
            rangeIndex--;
        }
        return line;
    }

    /**
     * Return the number of lines in the current range;  that is, the number
     * of lines after the current line, inclusive, but before the line at
     * the end of the range, exclusive.
     *
     * @return the number of lines in the current range
     */
    public int getRangeCount()
    {
        return rangeIndex - lineIndex;
    }

    /**
     * Get the index of the line at the end of the range.
     *
     * @return
     */
    public int getRangeIndex()
    {
        return rangeIndex;
    }

    /**
     * Return the number of capture groups from the most recent pattern or
     * regex match.
     *
     * @return
     */
    public int getGroupCount()
    {
        return groups.length;
    }

    /**
     * Return the capture group with the given index from the most recent
     * pattern or regex match.
     *
     * @param n
     * @return
     */
    public Object getGroup(int n)
    {
        return groups[n];
    }

    public Object[] getGroups()
    {
        return groups;
    }

    /**
     * Saves the given groups to be retrieved later with calls to
     * <code>getGroupCount()</code> and <code>getGroup(n)</code>, provided
     * <code>groups</code> is not null..
     *
     * @param groups
     * @return <code>true</code> it
     */
    public boolean setGroups(Object[] groups) {
        if (groups != null) {
            this.groups = groups;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether the current range is empty;  that is, if the range
     * index is the same as the line index.
     *
     * @return whether the current range is empty
     */
    public boolean isRangeEmpty()
    {
        return rangeIndex == lineIndex;
    }

    /**
     * Set the end of the current range to the line with the given index.  This
     * method will fail if the new range index is less than the current line
     * index, or larger than the number of lines in the object text.
     * 
     * If the range index is set to the current line index, then the range will
     * be empty.
     * 
     * @param index the new value for the range index
     * @return <code>true</code> if the range index was successfully set, 
     *         and <code>false</code> otherwise.
     */
    public boolean setRangeIndex(int index)
    {
        if (index < lineIndex || index > lines.size()) {
            return false;
        } else {
            rangeIndex = index;
            return true;
        }
    }

    /**
     * Directive: reset
     *
     * Resets the current line index and the range index to include all lines
     * in the object text.  Specifically, the current line is set to the first line
     * of the text being processed, and the range to the entire list of lines.
     *
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.  In the current implementation,
     *         this method always succeeds
     */
    public boolean reset()
    {
        lineIndex = 0;
        rangeIndex = lines.size();
        return true;
    }

    /**
     * Directive: range-reset
     *
     * Sets the range to include all lines from the current line to the end
     * of the text; that is, the range index is set to the number of lines in
     * the object text.
     *
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.  In the current implementation,
     *         this method always succeeds.
     */
    public boolean clearRangeIndex()
    {
        rangeIndex = lines.size();
        return true;
    }

    /**
     * Directive: range integer
     *
     * Sets the range to include the given number of lines, starting with the
     * current line.  If there are not that many lines to the end of the text,
     * then this directive fails, and the range is not changed.  The
     * argument must be a positive integer.
     *
     * @param integer the number of lines past the current line to set the
     *         range index.
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRange(int integer)
    {
        return setRangeIndex(lineIndex + integer);
    }

    /**
     * Directive: range regex
     *
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line strictly after the current line which matches the given
     * regular  expression, exclusive.  The regular expression must match the
     * entire line.
     *
     * If no line after the current line matches the regular expression, then
     * the directive fails, and the range is not changed.
     *
     * @param regex the regular expression to match.
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRangeMatches(String regex)
    {
        return setRangeMatches(Pattern.compile(regex));
    }

    /**
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line after the current line which matches the given pattern,
     * exclusive.  The pattern must match the entire line.
     *
     * If no line after the current line matches the given pattern, then
     * the function returns false, and the range is not changed.
     *
     * This method does not change the current capture group.
     *
     * @param regex the pattern to match.
     * @return <code>true</code> if the range is successfully changed, and
     *         <code>false</code> otherwise.
     */
    public boolean setRangeMatches(Pattern regex)
    {
        int n = lineIndex;
        while (++n < lines.size()) {
            if (match(regex, n)) {
                return setRangeIndex(n);
            }
        }
        return false;
    }

    /**
     * Directive: range-starts string
     *
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line after the current line which starts with the given
     * substring, exclusive.
     *
     * If no line after the current line starts with the given substring, then
     * the directive fails, and the range is not changed.
     *
     * @param string the substring to match
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRangeStartsWith(String string)
    {
        int index = lineIndex;
        while (++index < lines.size()) {
            if (getLine(index).startsWith(string)) {
                rangeIndex = index;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: range-ends string
     *
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line after the current line which ends with the given
     * substring, exclusive.
     *
     * If no line after the current line ends with the given substring, then
     * the directive fails, and the range is not changed.
     *
     * @param string the substring to match
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRangeEndsWith(String string)
    {
        int index = lineIndex;
        while (++index < lines.size()) {
            if (getLine(index).endsWith(string)) {
                rangeIndex = index;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: range-contains string
     *
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line after the current line which contains the given
     * substring, exclusive.
     *
     * If no line after the current line contains the given substring, then
     * the directive fails, and the range is not changed.
     *
     * @param string the substring to match
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRangeContains(String string)
    {
        int index = lineIndex;
        while (++index < lines.size()) {
            if (getLine(index).contains(string)) {
                rangeIndex = index;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: range-empty
     *
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line after the current line which is empty, exclusive.
     *
     * If no line after the current line is empty, then the directive fails,
     * and the range is not changed.
     *
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRangeIsEmpty()
    {
        int index = lineIndex;
        while (++index < lines.size()) {
            if (getLine(index).isEmpty()) {
                rangeIndex = index;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: range string
     *
     * Sets the range to include all lines from the current line, inclusive, to
     * the next line after the current line which is equal (not identical) to
     * the given string, exclusive.  "Equal" in this case means that Java
     * <code>String.equals()</code> returns <code>true</code>.
     *
     * If no line after the current line equals the given string, then the
     * directive fails, and the range is not changed.
     *
     * @param string the other string to match
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean setRangeEquals(String string)
    {
        int index = lineIndex;
        while (++index < lines.size()) {
            if (getLine(index).equals(string)) {
                rangeIndex = index;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: range-adjust integer
     *
     * Adjusts the end of the range by the given number of lines.  The 
     * parameter <code>n</code> may be negative.
     * 
     * If the requested line is equal to or comes before the current line, or if
     * the requested line is past the maximum range, then this directive fails,
     * and the range is not changed.
     * 
     * @param integer the number of lines to adjust the end of the range.
     * @return Succeeds if the range is successfully changed, and
     *         fails otherwise.
     */
    public boolean adjustRange(long integer)
    {
        return setRangeIndex(rangeIndex + (int)integer);
    }

    /**
     * Directive: prev
     *
     * Set the current line to be the line preceding it.  If there is no such
     * line, that is if the current line is already the first line in the
     * object text, then the directive fails, and the current
     * line is not changed.
     *
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLine()
    {
        return prevLine(1);
    }

    /**
     * Directive: prev integer
     *
     * Set the current line to be the line the given number of lines preceding
     * it.  The given parameter may be negative, but if the requested line
     * is before the first line in the object text, or is greater than or equal
     * to the end of the range, then this directive fails and
     * the current line is not changed.
     *
     * This method does not affect the end of the range.
     *
     * @param integer the number of lines to adjust the current line
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLine(int integer)
    {
        int index = lineIndex - integer;
        if (index < 0 || index >= rangeIndex) {
            return false;
        } else {
            lineIndex = index;
            return true;
        }
    }

    /**
     * Directive: prev regex
     *
     * Set the current line to be the closest line preceding it that matches
     * the given regular expression.  The entire line must match the regular
     * expression.
     *
     * If there is no line preceding the current line which matches the
     * regular expression, then this directive fails and
     * the current line is not changed.
     *
     * This method does not affect the end of the range, and it does not
     * change the current capture groups.
     *
     * @param regex the regular expression to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineMatches(String regex)
    {
        return prevLineMatches(Pattern.compile(regex));
    }

    /**
     * Set the current line to be the closest line preceding it that matches
     * the given pattern.  The entire line must match the pattern.
     *
     * If there is no line preceding the current line which matches the
     * pattern, then this directive fails and
     * the current line is not changed.
     *
     * This method does not affect the end of the range, and it does not
     * change the current capture groups.
     *
     * @param regex the pattern to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineMatches(Pattern regex)
    {
        int n = this.lineIndex;
        while (--n >= 0) {
            if (match(regex, n)) {
                return setLineIndex(n);
            }
        }
        return false;
    }

    /**
     * Directive: prev-starts string
     *
     * Set the current line to be the closest line preceding it that starts
     * with the given substring.
     *
     * If there is no line preceding the current line which starts with the
     * substring, then this directive fails and
     * the current line is not changed.
     *
     * This method does not affect the end of the range.
     *
     * @param string  the substring to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineStartsWith(String string)
    {
        int n = this.lineIndex;
        while (--n >= 0) {
            if (getLine(n).startsWith(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: prev-ends string
     *
     * Set the current line to be the closest line preceding it that ends
     * with the given substring.
     *
     * If there is no line preceding the current line which ends with the
     * substring, then this directive fails and
     * the current line is not changed.
     *
     * This method does not affect the end of the range.
     *
     * @param string  the substring to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineEndsWith(String string)
    {
        int n = this.lineIndex;
        while (--n >= 0) {
            if (getLine(n).endsWith(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: prev-contains string
     *
     * Set the current line to be the closest line preceding it that contains
     * the given substring.
     *
     * If there is no line preceding the current line which contains the
     * substring, then this directive fails and
     * the current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @param string  the substring to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineContains(String string)
    {
        int n = this.lineIndex;
        while (--n >= 0) {
            if (getLine(n).contains(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: prev-empty
     *
     * Set the current line to be the closest line preceding it that is empty.
     *
     * If there is no line preceding the current line which is empty, then
     * this directive fails and the current line is not
     * changed.
     *
     * This directive does not affect the end of the range.
     *
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineIsEmpty()
    {
        int n = this.lineIndex;
        while (--n >= 0) {
            if (getLine(n).isEmpty()) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: prev string
     *
     * Set the current line to be the closest line preceding it that equals
     * the given string.  "Equals" means that the method <code>equals()</code>
     * returns <code>true</code>.
     *
     * If there is no line preceding the current line which equals the
     * string, then this directive fails and
     * the current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @param string the other string to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean prevLineEquals(String string)
    {
        int n = this.lineIndex;
        while (--n >= 0) {
            if (getLine(n).equals(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: next
     *
     * Set the current line to be the line following it.  If the line following
     * it is the end of the current range, that is if
     *
     *     <code>lineIndex + 1 == rangeIndex</code>
     *
     * then the directive fails, and the current line is
     * not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLine()
    {
        return nextLine(1);
    }

    /**
     * Directive: next integer
     *
     * Set the current line to be the line the given number of lines following
     * it.  The given parameter may be negative, but if the requested line
     * is before the first line in the object text, or is greater than or equal
     * to the end of the range, then this directive fails and
     * the current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @param integer the number of lines to adjust the current line
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLine(int integer)
    {
        if (lineIndex + integer < rangeIndex) {
            lineIndex += integer;
            return true;
        }
        return false;
    }

    /**
     * Directive: next regex
     *
     * Set the current line to be the closest line following, but before the
     * end of the current range, it that matches the given regular expression.
     * The entire line must match the regular expression.
     *
     * If there is no line following it, but before the end of the range,
     * which matches the regular expression, then this directive fails
     * and the current line is not changed.
     *
     * This directive does not affect the end of the range, and it does not
     * change the current capture groups.
     *
     * @param regex the regular expression to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLineMatches(String regex)
    {
        return nextLineMatches(Pattern.compile(regex));
    }

    /**
     * Set the current line to be the closest line following, but before the
     * end of the current range, that matches the given pattern.
     * The entire line must match the regular expression.
     *
     * If there is no line following it, but before the end of the range,
     * which matches the pattern, then this method returns
     * <code>false</code> and the current line is not changed.
     *
     * This method does not affect the end of the range, and it does not
     * change the current capture groups.
     *
     * @param regex the regular expression to match.
     * @return <code>true</code> if the current line is successfully changed,
     *         and <code>false</code> otherwise.
     */
    public boolean nextLineMatches(Pattern regex)
    {
        int n = this.lineIndex;
        while (++n < rangeIndex) {
            if (match(regex, n)) {
                return setLineIndex(n);
            }
        }
        return false;
    }

    /**
     * Directive: next-starts string
     *
     * Set the current line to be the closest line following, but before the
     * end of the current range, it that starts with the given substring.
     * The entire line must match the regular expression.
     *
     * If there is no line following it, but before the end of the range,
     * which starts with the given substring, then this directive fails,
     * and the current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @param string the substring to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLineStartsWith(String string)
    {
        int n = this.lineIndex;
        while (++n < rangeIndex) {
            if (getLine(n).startsWith(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: next-ends string
     *
     * Set the current line to be the closest line following, but before the
     * end of the current range, it that ends with the given substring.
     * The entire line must match the regular expression.
     *
     * If there is no line following it, but before the end of the range,
     * which ends with the given substring, then this directive fails
     * and the current line is not changed.
     *
     * This method does not affect the end of the range.
     *
     * @param string the substring to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails  otherwise.
     */
    public boolean nextLineEndsWith(String string)
    {
        int n = this.lineIndex;
        while (++n < rangeIndex) {
            if (getLine(n).endsWith(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: next-contains string
     *
     * Set the current line to be the closest line following, but before the
     * end of the current range, it that contains the given substring.
     * The entire line must match the regular expression.
     *
     * If there is no line following it, but before the end of the range,
     * which contains the given substring, then this directive fails
     * and the current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @param string the substring to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLineContains(String string)
    {
        int n = this.lineIndex;
        while (++n < rangeIndex) {
            if (getLine(n).contains(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: next-empty
     *
     * Set the current line to be the closest line following it, but before the
     * end of the current range, that is empty.
     *
     * If there is no line following it, but before the end of the range,
     * which is empty, then this directive fails and the
     * current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLineIsEmpty()
    {
        int n = this.lineIndex;
        while (++n < rangeIndex) {
            if (getLine(n).isEmpty()) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: next string
     *
     * Set the current line to be the closest line following, but before the
     * end of the current range, it that equals  the given substring.
     * The entire line must match the regular expression.  "Equals" means that
     * the Java method <code>String.equals()</code> returns <code>true</code>.
     *
     * If there is no line following it, but before the end of the range,
     * which equals the given substring, then this directive fails
     * and the current line is not changed.
     *
     * This directive does not affect the end of the range.
     *
     * @param string the other string to match.
     * @return Succeeds if the current line is successfully changed,
     *         and fails otherwise.
     */
    public boolean nextLineEquals(String string)
    {
        int n = this.lineIndex;
        while (++n < rangeIndex) {
            if (getLine(n).equals(string)) {
                this.lineIndex = n;
                return true;
            }
        }
        return false;
    }

    /**
     * Directive: insert string
     *
     * Inserts the given line into the object text before the current line.
     *
     * The current range is not affected.
     *
     * @param string line to insert
     * @return Succeeds if the line is correctly inserted, and fails
     *         otherwise.  The current implementation always succeeds.
     */
    public boolean insertLine(String string)
    {
        lines.add(lineIndex, string);
        lineIndex++;
        rangeIndex++;
        return true;
    }

    /**
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and arguments, and inserts the new line into the
     * object text before the current line.  If the message cannot be formatted
     * correctly, then this method returns <code>false</code> and the string
     * is not inserted.
     *
     * The current range is not affected.
     *
     * @param format the MessageFormat string
     * @param args the arguments for the MessageFormat string
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.  The current implementation will fail only if the
     *         MessageFormat fails.
     */
    public boolean insertLine(String format, Object... args)
    {
        try {
            return insertLine(MessageFormat.format(format, args));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Directive: insert format
     *
     * Formats a new string by calling <code>MessageFormat.format()</code>
     * the given format string and the capture groups from any previous match,
     * and inserts the new line into the object text before the current line.
     * If the message cannot be formatted correctly, then this directive fails,
     * and the string is not inserted.
     *
     * The current range is not affected.
     *
     * @param format the MessageFormat string
     * @return Succeeds if the line is correctly inserted, and fails
     *         otherwise.  The current implementation will fail only if the
     *         MessageFormat fails.
     */
    public boolean insertFormat(String format)
    {
        return insertLine(format, groups);
    }

    /**
     * Rewrites the current line according to the given pattern and format, and
     * inserts the resulting line into the object text before the current line.
     *
     * First, the given pattern is applied to the current line.  If the pattern
     * does not match, then this method returns <code>false</code> and the
     * object text is not changed.  If the pattern matches, then its capture
     * groups are collected as arguments the format string.  The new line is
     * formatted by calling <code>MessageFormat.format()</code> with the given
     * format string and the capture groups as arguments.
     *
     * The current range is not affected.
     *
     * @param pattern the pattern to match to the current line
     * @param format the MessageFormat string to format the new line
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.  The current implementation will fail if the pattern
     *         does not match the current line, or if the MessageFormat fails.
     */
    public boolean insertRewrite(Pattern pattern, String format)
    {
        return match(pattern) ? insertLine(format, groups) : false;
    }

    /**
     * Directive: replace regex format
     *
     * Rewrites the current line according to the given regular expression and
     * format string, and inserts the resulting line into the object text
     * before the current line.
     *
     * First, the given regular expression is applied to the current line.
     * If the regex does not match, then this directive fails
     * and the object text is not changed.  If the regex matches, then its
     * capture groups are collected as arguments the format string.  The new
     * line is formatted by calling <code>MessageFormat.format()</code> with
     * the given format string and the capture groups as arguments.
     *
     * The current range is not affected.
     *
     * @param regex the regular expression to match to the current line
     * @param format the MessageFormat string to format the new line
     * @return Succeeds if the current line is correctly replaced, and fails
     *         otherwise.  The current implementation will fail if the pattern
     *         does not match the current line, or if the MessageFormat fails.
     */
    public boolean insertRewrite(String regex, String format)
    {
        return insertRewrite(Pattern.compile(regex), format);
    }

    /**
     * Directive: insert-after string
     *
     * Inserts the given line into the object text after the current line.
     *
     * The new line is added to the current range.
     *
     * @param string line to insert
     * @return Succeeds if the line is correctly inserted, and fails
     *         otherwise.  The current implementation always returns
     *         <code>true</code>.
     */
    public boolean insertLineAfter(String string)
    {
        if (lineIndex >= rangeIndex) {
            return false;
        } else {
            lines.add(++lineIndex, string);
            rangeIndex++;
            return true;
        }
    }

    /**
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and arguments, and inserts the new line into the
     * object text after the current line.  If the message cannot be formatted
     * correctly, then this method returns <code>false</code> and the string
     * is not inserted.
     *
     * The current range is not affected, except that the new line becomes
     * part of the range.
     *
     * @param format the MessageFormat string
     * @param args the arguments for the MessageFormat string
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.  The current implementation will fail only if the
     *         MessageFormat fails.
     */
    public boolean insertLineAfter(String format, Object... args)
    {
        try {
            return insertLineAfter(MessageFormat.format(format, args));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Directive: insert-after format
     *
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and the capture groups from any previous match,
     * and inserts the new line into the object text after the current line.
     * If the message cannot be formatted correctly, then this directive fails
     * and the string is not inserted.
     *
     * The current range is not affected, except that the new line becomes
     * part of the range.
     *
     * @param format the MessageFormat string
     * @return Succeeds if the line is correctly inserted, and fails
     *         otherwise.  The current implementation will fail only if the
     *         MessageFormat fails.
     */
    public boolean insertFormatAfter(String format)
    {
        return insertLineAfter(format, groups);
    }

    /**
     * Rewrites the current line according to the given pattern and format, and
     * inserts the resulting line into the object text after the current line.
     *
     * First, the given pattern is applied to the current line.  If the pattern
     * does not match, then this method returns <code>false</code> and the
     * object text is not changed.  If the pattern matches, then its capture
     * groups are collected as arguments the format string.  The new line is
     * formatted by calling <code>MessageFormat.format()</code> with the given
     * format string and the capture groups as arguments.
     *
     * The current range is not affected, except that the new line becomes
     * part of the range.
     *
     * @param pattern the pattern to match to the current line
     * @param format the MessageFormat string to format the new line
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.  The current implementation will fail if the pattern
     *         does not match the current line, or if the MessageFormat fails.
     */
    public boolean insertRewriteAfter(Pattern pattern, String format)
    {
        return match(pattern) ? insertLineAfter(format, groups) : false;
    }

    /**
     * Directive: insert-after regex format
     *
     * Rewrites the current line according to the given regular expression and
     * format string, and inserts the resulting line into the object text
     * after the current line.
     *
     * First, the given regular expression is applied to the current line.
     * If the regex does not match, then this directive fails,
     * and the object text is not changed.  If the regex matches, then its
     * capture groups are collected as arguments the format string.  The new
     * line is formatted by calling <code>MessageFormat.format()</code> with
     * the given format string and the capture groups as arguments.
     *
     * The current range is not affected, except that the new line becomes
     * part of the range.
     *
     * @param regex the regular expression to match to the current line
     * @param format the MessageFormat string to format the new line
     * @return Succeeds if the line is correctly inserted, and fails
     *         otherwise.  The current implementation will fail if the pattern
     *         does not match the current line, or if the MessageFormat fails.
     */
    public boolean insertRewriteAfter(String regex, String format)
    {
        return insertRewriteAfter(Pattern.compile(regex), format);
    }

    /**
     * Directive: append string
     *
     * Inserts the given line after the last line in the current range.  If the
     * range is currently empty, then the new line becomes the new current line.
     * The end of the range is adjusted to include the appended line.
     *
     * @param string the line to insert
     * @return Succeeds if the line is correctly appended, and fails
     *         otherwise.  The current implementation always returns
     *         <code>true</code>.
     */
    public boolean appendLine(String string)
    {
        if (rangeIndex == lines.size()) {
            lines.add(string);
            rangeIndex = lines.size();
        } else {
            lines.add(rangeIndex, string);
            rangeIndex++;
        }
        return true;
    }

    /**
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and arguments, and inserts the new line after
     * the last line in the current range.  If the range is currently empty,
     * then the new line becomes the new current line.  If the message cannot
     * be formatted correctly, then this method returns <code>false</code> and
     * the string is not inserted.
     *
     * @param format the MessageFormat string
     * @param args the arguments for the MessageFormat string
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.  The current implementation will fail only if the
     *         MessageFormat fails.
     */
    public boolean appendLine(String format, Object... args)
    {
        try {
            return appendLine(MessageFormat.format(format, args));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Directive: append format
     *
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and the capture groups from any previous match,
     * and inserts the new line after the last line in the current range.  If
     * the range is currently empty, then the new line becomes the new current
     * line.  If the message cannot be formatted correctly, then this directive
     * fails, and the string is not inserted.
     *
     * @param format the MessageFormat string
     * @return Succeeds if the line is correctly appended, and fails
     *         otherwise.  The current implementation will fail only if the
     *         MessageFormat fails.
     */
    public boolean appendFormat(String format)
    {
        return appendLine(format, groups);
    }

    /**
     * Rewrites the current line according to the given pattern and format, and
     * inserts the resulting line into the object text after the last line in
     * the current range..
     *
     * First, the given pattern is applied to the current line.  If the pattern
     * does not match, then this method returns <code>false</code> and the
     * object text is not changed.  If the pattern matches, then its capture
     * groups are collected as arguments the format string.  The new line is
     * formatted by calling <code>MessageFormat.format()</code> with the given
     * format string and the capture groups as arguments.
     *
     * @param pattern the pattern to match to the current line
     * @param format the MessageFormat string to format the new line
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.  The current implementation will fail if the pattern
     *         does not match the current line, or if the MessageFormat fails.
     */
    public boolean appendRewrite(Pattern pattern, String format)
    {
        return match(pattern) ? appendLine(format, groups) : false;
    }

    /**
     * Directive: append regex format
     *
     * Rewrites the current line according to the given regular expression and
     * format string, and inserts the resulting line into the object text
     * after the last line in the current range.
     *
     * First, the given regular expression is applied to the current line.
     * If the regex does not match, then this directive fails,
     * and the object text is not changed.  If the regex matches, then its
     * capture groups are collected as arguments the format string.  The new
     * line is formatted by calling <code>MessageFormat.format()</code> with
     * the given format string and the capture groups as arguments.
     *
     * @param regex the regular expression to match to the current line
     * @param format the MessageFormat string to format the new line
     * @return Succeeds if the line is correctly appended, and fails
     *         otherwise.  The current implementation will fail if the pattern
     *         does not match the current line, or if the MessageFormat fails.
     */
    public boolean appendRewrite(String regex, String format)
    {
        return appendRewrite(Pattern.compile(regex), format);
    }

    /**
     * Directive: remove
     *
     * Removes the current line from the object text.  The line following the
     * current line becomes the new current line.
     *
     * This directive will fail if the current range is empty;  that is if the
     * current line index equals the range index.
     *
     * @return Succeeds if the line is correctly removed, and fails
     *         otherwise.
     */
    public boolean removeLine()
    {
        if (lineIndex == rangeIndex) {
            return false;
        } else {
            lines.remove(lineIndex);
            rangeIndex--;
            return true;
        }
    }

    /**
     * Directive: remove-range
     *
     * Removes all lines from the object text, starting from the current line,
     * inclusive, up to end of the range.  If this directive succeeds, the
     * range will be empty, that is, the current line index will equal the
     * range index.
     *
     * @return Succeeds if the lines are correctly removed, and fails
     *         otherwise.  This current implementation always returns true;
     */
    public boolean removeRange()
    {

        while (removeLine()) {
            // continue to next line
        }
        return true;
    }

    /**
     * Directive: replace string
     *
     * Replaces the current line with the given line.  This directive fails if the
     * current range is empty.
     *
     * @param string the new line to replace the current line.
     * @return Succeeds if the current line is correctly replaced, and fails
     *         otherwise.
     */
    public boolean replaceLine(String string)
    {
        if (lines.isEmpty()) {
            return false;
        } else {
            lines.set(lineIndex, string);
            return true;
        }
    }

    /**
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and arguments, and replaces the current line
     * with the new line.  If the message cannot be formatted correctly, then
     * this method returns <code>false</code> and the string is not inserted.
     *
     * @param format the MessageFormat string
     * @param args the arguments for the MessageFormat string
     * @return <code>true</code> if the method succeeds, and <code>false</code>
     *         otherwise.
     */
    public boolean replaceLine(String format, Object... args)
    {
        try {
            return replaceLine(MessageFormat.format(format, args));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Directive: replace format
     *
     * Formats a new string by calling <code>MessageFormat.format()</code> with
     * the given format string and the capture groups from any previous match,
     * and replaces the current line with the new line.  If the message cannot
     * be formatted correctly, then this directive fails
     * and the string is not inserted.
     *
     * @param format the MessageFormat string
     * @return Succeeds if the current line is correctly replaced, and fails
     *         otherwise.
     */
    public boolean replaceFormat(String format)
    {
        return replaceLine(format, groups);
    }

    /**
     * Directive: contains string
     *
     * Determines whether the current line contains the given substring.  If the
     * current range is empty, then this directive fails.
     *
     * @param string the substring to find
     * @return Succeeds if the current line contains the given substring, and
     *         fails otherwise.
     */
    public boolean contains(String string)
    {
        return lines.get(lineIndex).contains(string);
    }

    /**
     * Directive: starts string
     *
     * Determines whether the current line starts with the given substring.  If the
     * current range is empty, then this directive fails.
     *
     * @param string the substring to find
     * @return Succeeds if the current line starts with the given substring,
     *         and fails otherwise
     */
    public boolean startsWith(String string)
    {
        try {
            return getLine().startsWith(string);
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     * Directive: ends string
     *
     * Determines whether the current line ends with the given substring.  If the
     * current range is empty, then this directive fails
     *
     * @param string the substring to find
     * @return Succeeds if the current line ends with the given substring,
     *         and fails otherwise.
     */
    public boolean endsWith(String string)
    {
        try {
            return getLine().endsWith(string);
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     * Directive: empty
     *
     * Determines whether the current line is empty.  If the
     * current range is empty, then this directive fails.
     *
     * @return Succeeds if the current line is empty, and fails otherwise.
     */
    public boolean isEmpty()
    {
        try {
            return getLine().isEmpty();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     * Directive: equals string
     *
     * Returns whether the current line equals the given other line.  If the
     * current range is empty, then this directive fails
     *
     * @param string the substring to find
     * @return Succeeds if the current line equals the given other line, and
     *         fails otherwise.
     */
    public boolean equals(String string)
    {
        try {
            return getLine().equals(string);
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     *
     * @param target
     * @param replacement
     * @return
     */
    public boolean replace(CharSequence target, CharSequence replacement)
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().replace(target, replacement));
        } else {
            return false;
        }
    }

    /**
     *
     * @param oldChar
     * @param newChar
     * @return
     */
    public boolean replace(char oldChar, char newChar)
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().replace(oldChar, newChar));
        } else {
            return false;
        }
    }

    /**
     * Directive: replace-all regex string
     *
     * Replaces all substrings which match the regular expression with the
     * replacement string in the current line. This directive fails if the
     * current range is empty.
     *
     * @param regex
     * @param string
     * @return Succeeds if the current line is correctly rewritten, and fails
     *         otherwise.
     */
    public boolean replace(String regex, String string)
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().replaceAll(regex, string));
        } else {
            return false;
        }
    }

    /**
     * Directive: replace-first regex string
     *
     * Replaces the first substring which match the regular expression with the
     * replacement string in the current line. This directive fails if the
     * current range is empty.
     *
     * @param regex
     * @param string
     * @return Succeeds if the current line is correctly rewritten, and fails
     *         otherwise.
     */
    public boolean replaceFirst(String regex, String string)
    {
        if (lineIndex >= rangeIndex) {
            return false;
        }
        String result = getLine().replaceFirst(regex, string);
        return result.equals(getLine()) ? false : replaceLine(result);
    }

    /**
     * Directive: catenate integer
     *
     * Concatenate the current line together with the next given number of
     * lines.  The concatenated line replaces the current line, and all
     * the following lines are removed from the list.
     *
     * @param integer
     * @return Succeeds if the lines are correctly concatenated together,
     *         and fails otherwise.
     */
    public boolean catenate(long integer)
    {
        if (lineIndex + integer >= rangeIndex) {
            return false;
        }
        StringBuilder buffer = new StringBuilder(getLine());
        while (integer-- > 0) {
            buffer.append(deleteLine(lineIndex+1));
        }
        return replaceLine(buffer.toString());
    }

    /**
     * Directive: catenate
     *
     * Concatenate the current line together with the following line.  The
     * concatenated line replaces the current line, and the following line
     * is removed from the list.
     *
     * @return Succeeds if the lines are correctly concatenated together,
     *         and fails otherwise.
     */
    public boolean catenate()
    {
        return catenate(1);
    }

    public boolean slice(int fromIndex)
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().substring(fromIndex));
        } else {
            return false;
        }
    }

    public boolean slice(int fromIndex, int toIndex)
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().substring(fromIndex, toIndex));
        } else {
            return false;
        }
    }

    public boolean toLowerCase()
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().toLowerCase());
        } else {
            return false;
        }
    }

    public boolean toUpperCase()
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().toUpperCase());
        } else {
            return false;
        }
    }

    public boolean split(String regex)
    {
        if (lineIndex < rangeIndex) {
            groups = getLine().split(regex);
            return true;
        } else {
            return false;
        }
    }

    public boolean trim()
    {
        if (lineIndex < rangeIndex) {
            return replaceLine(getLine().trim());
        } else {
            return false;
        }
    }

    /**
     * Attempts to match the current line to the given pattern.  If
     * the line matches, the method returns <code>true</code>, and the
     * captured groups are saved to be retrieved later with calls to
     * <code>getGroupCount()</code> and <code>getGroup(n)</code>.
     *
     * @param pattern
     * @return <code>true</code> if the current line matched the given pattern,
     * and <code>false</code> otherwise.
     */
    public boolean match(Pattern pattern)
    {
        try {
            return setGroups(getCaptureGroups(pattern, getLine()));
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Directive: match regex
     *
     * Attempts to match the current line to the given regular expression.  If
     * the line matches, the directive succeeds, and the
     * captured groups are saved to be used later in formats.
     *
     * @param regex
     * @return Succeeds if the current line matched the given pattern,
     *         and fails otherwise.
     */
    public boolean match(String regex)
    {
        return match(Pattern.compile(regex));
    }

    /**
     * Attempts to match the line at the given index to the given pattern.
     * If the line matches, the method returns <code>true</code>, and the
     * captured groups are saved to be retrieved later with calls to
     * <code>getGroupCount()</code> and <code>getGroup(n)</code>.
     *
     * @param pattern
     * @param index
     * @return <code>true</code> if the current line matched the given pattern,
     * and <code>false</code> otherwise.
     */
    public boolean match(Pattern pattern, int index)
    {
        try {
            return setGroups(getCaptureGroups(pattern, getLine(index)));
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Attempts to match the line at the given index to the regular expression.
     * If the line matches, the method returns <code>true</code>, and the
     * captured groups are saved to be retrieved later with calls to
     * <code>getGroupCount()</code> and <code>getGroup(n)</code>.
     *
     * @param regex
     * @param index
     * @return <code>true</code> if the current line matched the given pattern,
     * and <code>false</code> otherwise.
     */
    public boolean match(String regex, int index)
    {
        return match(Pattern.compile(regex), index);
    }

    /**
     * Matches the current line to the given pattern, formats a new line
     * using the matching groups from the pattern, and replaces the current
     * line with the new line.  If the current line does not match the
     * pattern, then this method returns false, and the line is not changed.
     *
     * If the pattern matches, the capture groups are saved and
     * can be accessed by calling <code>getGroupCount()</code> and
     * <code>getGroup(n)</code>.
     *
     * @param pattern
     * @param format
     * @return <code>true</code> if the current line matched the given pattern,
     * and <code>false</code> otherwise.
     */
    public boolean rewrite(Pattern pattern, String format)
    {
        return match(pattern) ? replaceLine(format, groups) : false;
    }

    /**
     * Directive: replace regex format
     *
     * Matches the current line to the given regular expression, formats a new
     * line using the matching groups from the regex, and replaces the current
     * line with the new line.  If the current line does not match the
     * pattern, then this directive fails, and the line is not changed.
     *
     * If the regular expression matches, the capture groups are saved and
     * can be accessed later by formats.
     *
     * @param regex
     * @param format
     * @return Succeeds if the current line matched the given regex,
     *         and fails otherwise.
     */
    public boolean rewrite(String regex, String format)
    {
        return rewrite(Pattern.compile(regex), format);
    }

}
