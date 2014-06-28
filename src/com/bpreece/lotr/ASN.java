/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bpreece.lotr;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ben
 */
public abstract class ASN
        implements FrodoConstants
{
    public static interface TConstant
    {
    }

    public static class TString implements TConstant
    {
        public final String string;

        public TString(String string)
        {
            this.string = string;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "TString(\"{0}\")", string);
        }

        @Override
        public String toString()
        {
            return "TString[" + string + "]";
        }

    }

    public static class TRegex implements TConstant
    {
        public final String regex;

        public TRegex(String regex)
        {
            this.regex = regex;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "TRegex(\"{0}\")", regex);
        }

        @Override
        public String toString()
        {
            return "TRegex[" + regex + "]";
        }

    }

    public static class TFormat implements TConstant
    {
        public final String format;

        public TFormat(String format)
        {
            this.format = format;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "TFormat(\"{0}\")", format);
        }

        @Override
        public String toString()
        {
            return "TFormat[" + format + "]";
        }

    }

    public static class TInteger implements TConstant
    {
        public final long value;

        public TInteger(long value)
        {
            this.value = value;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "TInteger({0})", value);
        }

        @Override
        public String toString()
        {
            return "TInteger[" + value + "]";
        }

    }

    public static class TFloat implements TConstant
    {
        public final double value;

        public TFloat(double value)
        {
            this.value = value;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "TFloat({0})", value);
        }

        @Override
        public String toString()
        {
            return "TFloat[" + value + "]";
        }

    }

    public static class Definition implements Command
    {
        public final java.lang.String id;
        public final TConstant value;

        public Definition(java.lang.String id, TConstant value)
        {
            this.id = id;
            this.value = value;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "Definition({0}=\"{1}\")", new Object[]{id, value});
        }

        public boolean execute(LOTR lotr)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    public static interface Command
    {
        public boolean execute(LOTR lotr);

    }

    public static class Directive implements Command
    {
        public final int command;
        public final Object[] parameters;

        public Directive(int command, Object... parameters)
        {
            this.command = command;
            this.parameters = parameters;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "Directive({0})", FrodoConstants.tokenImage[command]);
        }

        public boolean execute(LOTR lotr)
        {
            return ASN.apply(lotr, command, parameters);
        }

    }

    public static class While implements Command
    {
        private final Command command;

        public While(Command command)
        {
            this.command = command;
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "While");
        }

        public boolean execute(LOTR lotr)
        {
            while (command.execute(lotr)) {
            }
            return true;
        }

    }

    public static class Sequence implements Command
    {
        public final ArrayList<Command> commandList;

        public Sequence(Command... commands)
        {
            this.commandList = new ArrayList<Command>();
//            Logger.getLogger(ASN.class.getName()).log(Level.SEVERE,
//                    "Sequence");
        }

        public void add(Command command)
        {
            commandList.add(command);
        }

        public boolean execute(LOTR lotr)
        {
            for (Command command : commandList) {
                if (!command.execute(lotr)) {
                    return false;
                }
            }
            return true;
        }

    }

    public static class Disjunction implements Command
    {
        public final List<Command> commandList;

        public Disjunction()
        {
            commandList = new ArrayList<Command>();
        }

        public void add(Command command)
        {
            commandList.add(command);
        }

        public boolean execute(LOTR lotr)
        {
            for (Command command : commandList) {
                if (command.execute(lotr)) {
                    return true;
                }
            }
            return false;
        }

    }

    public static class Script
    {
        private final Command command;

        Script(Command command)
        {
            this.command = command;
        }

        public boolean execute(LOTR lotr)
        {
            return command.execute(lotr);
        }

    }

    private ASN()
    {
    }

    private static boolean apply(LOTR lotr, int command, Object[] parameters)
    {
        try {
            switch (command) {
            case T_ABORT:
                if (parameters.length == 1) {
                    if (parameters[0] instanceof TString) {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.INFO, ((TString) parameters[0]).string);
                    } else if (parameters[0] instanceof TFormat) {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.INFO, ((TFormat) parameters[0]).format,
                                    lotr.getGroups());
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Abort: unexpected parameter ({0})",
                                    parameters[0]);
                    }
                }
                System.exit(-1);
                break;
            case T_LOG:
                if (parameters.length == 0) {
                    Logger.getLogger(ASN.class.getName()).log(Level.INFO,
                                                              "Log ...");
                    return true;
                } else if (parameters.length == 1) {
                    if (parameters[0] instanceof TString) {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.INFO, ((TString) parameters[0]).string);
                        return true;
                    } else if (parameters[0] instanceof TFormat) {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.INFO, ((TFormat) parameters[0]).format,
                                    lotr.getGroups());
                        return true;
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Fail: unexpected parameter ({0})",
                                    parameters[0]);
                        return false;
                    }
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Next: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            case T_FAIL:
                if (parameters.length > 0) {
                    if (parameters[0] instanceof TString) {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    ((TString) parameters[0]).string);
                    } else if (parameters[0] instanceof TFormat) {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    ((TFormat) parameters[0]).format,
                                    lotr.getGroups());
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Fail: unexpected parameter ({0})",
                                    parameters[0]);
                        return false;
                    }
                }
                return false;
            case T_RESET:
                return lotr.reset();
            case T_EMPTY:
                return lotr.isEmpty();
            case T_EQUALS:
                return lotr.equals(((TString) parameters[0]).string);
            case T_STARTS:
                return lotr.startsWith(((TString) parameters[0]).string);
            case T_ENDS:
                return lotr.endsWith(((TString) parameters[0]).string);
            case T_CONTAINS:
                return lotr.contains(((TString) parameters[0]).string);
            case T_RANGE_RESET:
                return lotr.clearRangeIndex();
            case T_RANGE:
                if (parameters.length == 1) {
                    if (parameters[0] instanceof TString) {
                        return lotr.setRangeEquals(
                                ((TString) parameters[0]).string);
                    } else if (parameters[0] instanceof TRegex) {
                        return lotr.setRangeMatches(
                                ((TRegex) parameters[0]).regex);
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Next: unexpected parameter ({0})",
                                    parameters[0]);
                        return false;
                    }
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Next: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            case T_RANGE_EMPTY:
                return lotr.setRangeIsEmpty();
            case T_RANGE_STARTS:
                return lotr.setRangeStartsWith(((TString) parameters[0]).string);
            case T_RANGE_ENDS:
                return lotr.setRangeEndsWith(((TString) parameters[0]).string);
            case T_RANGE_CONTAINS:
                return lotr.setRangeContains(((TString) parameters[0]).string);
            case T_RANGE_ADJUST:
                return lotr.adjustRange(((TInteger) parameters[0]).value);
            case T_NEXT:
                if (parameters.length == 0) {
                    return lotr.nextLine();
                } else if (parameters.length == 1) {
                    if (parameters[0] instanceof TString) {
                        return lotr.nextLineEquals(
                                ((TString) parameters[0]).string);
                    } else if (parameters[0] instanceof TInteger) {
                        return lotr.nextLine(
                                (int) ((TInteger) parameters[0]).value);
                    } else if (parameters[0] instanceof TRegex) {
                        return lotr.nextLineMatches(
                                ((TRegex) parameters[0]).regex);
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Next: unexpected parameter ({0})",
                                    parameters[0]);
                        return false;
                    }
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Next: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            case T_NEXT_EMPTY:
                return lotr.nextLineIsEmpty();
            case T_NEXT_STARTS:
                return lotr.nextLineStartsWith(((TString) parameters[0]).string);
            case T_NEXT_ENDS:
                return lotr.nextLineEndsWith(((TString) parameters[0]).string);
            case T_NEXT_CONTAINS:
                return lotr.nextLineContains(((TString) parameters[0]).string);
            case T_PREV:
                if (parameters.length == 0) {
                    return lotr.nextLine();
                } else if (parameters.length == 1) {
                    if (parameters[0] instanceof TString) {
                        return lotr.prevLineEquals(
                                ((TString) parameters[0]).string);
                    } else if (parameters[0] instanceof TRegex) {
                        return lotr.prevLineMatches(
                                ((TRegex) parameters[0]).regex);
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Prev: unexpected parameter ({0})",
                                    parameters[0]);
                        return false;
                    }
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Prev: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            case T_PREV_EMPTY:
                return lotr.prevLineIsEmpty();
            case T_PREV_STARTS:
                return lotr.prevLineStartsWith(((TString) parameters[0]).string);
            case T_PREV_ENDS:
                return lotr.prevLineEndsWith(((TString) parameters[0]).string);
            case T_PREV_CONTAINS:
                return lotr.prevLineContains(((TString) parameters[0]).string);
            case T_INSERT:
                if (parameters[0] instanceof TString) {
                    return lotr.insertLine(((TString) parameters[0]).string);
                } else if (parameters[0] instanceof TFormat) {
                    return lotr.insertFormat(((TFormat) parameters[0]).format);
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Insert: unexpected parameter ({0})",
                                parameters[0]);
                    return false;
                }
            case T_INSERT_AFTER:
                if (parameters[0] instanceof TString) {
                    return lotr.insertLineAfter(((TString) parameters[0]).string);
                } else if (parameters[0] instanceof TFormat) {
                    return lotr.insertFormatAfter(
                            ((TFormat) parameters[0]).format);
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Insert-after: unexpected parameter ({0})",
                                parameters[0]);
                    return false;
                }
            case T_APPEND:
                if (parameters[0] instanceof TString) {
                    return lotr.appendLine(((TString) parameters[0]).string);
                } else if (parameters[0] instanceof TFormat) {
                    return lotr.appendFormat(((TFormat) parameters[0]).format);
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Append: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            case T_REMOVE:
                return lotr.removeLine();
            case T_REMOVE_RANGE:
                return lotr.removeRange();
            case T_REPLACE:
                if (parameters.length == 1) {
                    if (parameters[0] instanceof TString) {
                        return lotr.replaceLine(((TString) parameters[0]).string);
                    } else if (parameters[0] instanceof TFormat) {
                        return lotr.replaceFormat(
                                ((TFormat) parameters[0]).format);
                    } else {
                        Logger.getLogger(ASN.class.getName()).
                                log(Level.WARNING,
                                    "Replace: unexpected parameter ({0})",
                                    parameters[0]);
                        return false;
                    }
                } else if (parameters.length == 2) {
                    return lotr.rewrite(((TRegex) parameters[0]).regex,
                                        ((TFormat) parameters[1]).format);
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Replace: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            case T_REPLACE_FIRST:
                return lotr.replaceFirst(((TRegex) parameters[0]).regex,
                                         ((TString) parameters[1]).string);
            case T_REPLACE_ALL:
                return lotr.replace(((TRegex) parameters[0]).regex,
                                    ((TString) parameters[1]).string);
            case T_CATENATE:
                if (parameters.length == 0) {
                    return lotr.catenate();
                } else {
                    return lotr.catenate(((TInteger) parameters[0]).value);
                }
            case T_MATCH:
                if (parameters.length == 1) {
                    return lotr.match(((TRegex) parameters[0]).regex);
                } else {
                    Logger.getLogger(ASN.class.getName()).
                            log(Level.WARNING,
                                "Match: bad parameter count ({0})",
                                parameters.length);
                    return false;
                }
            default:
                throw new IllegalStateException("unknown command " + command);
            }
        } catch (Exception ex) {
            /* log and return false */
            Logger.getLogger(ASN.class.getName()).log(Level.WARNING, null, ex);
            return false;
        }

        // default action when nothing else works is always to return false
        Logger.getLogger(ASN.class.getName()).log(Level.WARNING, Integer.
                toString(command));
        return false;
    }

}
