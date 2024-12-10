package org.markdown;

import java.util.ArrayList;

public class List extends Element{
    private final ArrayList<Text> list;

    public static class Builder implements org.markdown.Builder {
        public static final int LIST = 0;
        public static final int TASKLIST = 1;
        public static final int ORDEREDLIST = 2;
        private ArrayList<Text> list;
        private int type;
        private ArrayList<Boolean> completelist;

        public Builder() {
            this.type = LIST;
            list = new ArrayList<>();
        }

        public void setVal(boolean val, int num) {
            if (type == TASKLIST) {
                completelist.set(num, val);
            }
        }

        public void add(Text text) {
            if (type == TASKLIST) {
                if (completelist == null) {
                    completelist = new ArrayList<>();
                }
                completelist.add(false);
            }

            list.add(text);
        }

        public boolean remove(int num) {
            boolean ret = true;
            if (type == TASKLIST) {
                ret = completelist.remove(num) != null;
            }

            ret = (list.remove(num) != null);
            return ret;
        }

        public void setTypeOfList(int number) {
            if (number < 3 && number >= 0) {
                type = number;
            } else {
                throw new IllegalArgumentException("Invalid list type");
            }

        }

        public List build() {
            switch (type) {
                case LIST:
                    return new List(list);
                case ORDEREDLIST:
                    return new OrderedList(list);
                case TASKLIST:
                    return new TaskList(list, completelist);
                default:
                    return null;
            }
        }
    }

    public static class TaskList extends List {
        ArrayList<Boolean> completeTasks;
        public TaskList(ArrayList<Text> list, ArrayList<Boolean> completeList) {
            super(list);
            if (list.size() != completeList.size()) {
                throw new IllegalArgumentException("Unequal list size");
            }

            completeTasks = new ArrayList<>(completeList);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            int num = 1;
            for (int i = 0; i < completeTasks.size(); i++) {
                str.append("- [");
                if (completeTasks.get(i)) {
                    str.append("x");
                } else {
                    str.append(" ");
                }
                str.append("] ").append(super.list.get(i)).append("\n");

            }

            return str.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof TaskList) {
                TaskList taskList = (TaskList) obj;
                return super.equals(taskList);
            }

            return false;
        }
    }

    public static class OrderedList extends List {
        public OrderedList(ArrayList<Text> list) {
            super(list);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            int num = 1;
            for (Text text : super.list) {
                str.append(num).append(" ").append(text).append("\n");
                num++;
            }

            return str.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof OrderedList) {
                OrderedList orderedList = (OrderedList) obj;
                return super.equals(orderedList);
            }

            return false;
        }
    }

    public Builder getBuilder() {
        return new List.Builder();
    }

    public List(ArrayList<Text> listOfText) {
        list = new ArrayList<>(listOfText);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Text text : list) {
            str.append("+ ").append(text).append("\n");
        }

        return str.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof List) {
            List list1 = (List) obj;
            return list.equals(list1.list);
        }

        return false;
    }
}
