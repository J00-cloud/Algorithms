public class CircularSuffixArray {
    private int len;
    private int[] indices;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        len = s.length();
        indices = new int[len];
        for (int i = 0; i < len; i++) {
            indices[i] = i;
        }

        quickSort(s, 0, (len - 1), 0);
    }

    public int length() {
        return len;
    }

    public int index(int i) {
        if (i < 0 || i > (length() - 1)) {
            throw new IllegalArgumentException();
        }
        return indices[i];
    }

    private char charAt(String s, int index, int depth) {
        return s.charAt((index + depth) % len);
    }

    private void exch(int i, int j) {
        int temp = indices[i];
        indices[i] = indices[j];
        indices[j] = temp;
    }

    private void quickSort(String s, int lo, int hi, int depth) {
        if (lo >= hi) {
            return;
        }
        else {
            int lt = lo;
            int gt = hi;
            int eq = lo + 1;
            char compChar = charAt(s, indices[lo], depth);

            while (eq <= gt) {
                char currentChar = charAt(s, indices[eq], depth);
                if (compChar < currentChar) {
                    exch(gt, eq);
                    gt--;
                }
                else if (compChar > currentChar) {
                    exch(lt, eq);
                    lt++;
                    eq++;
                }
                else {
                    eq++;
                }
            }

            // if there are same chars (many eq), do recursive sort on those
            if ((gt - lt) > 0 && depth < len) {
                quickSort(s, lt, gt, depth + 1);
            }
            quickSort(s, lo, (lt - 1), depth);
            quickSort(s, (gt + 1), hi, depth);

        }

    }

    public static void main(String[] args) {

    }
}
