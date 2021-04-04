import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

/** Project 3.
 *  Connor Wall
 *  cwall6
 *  500.112 Gateway Computing Java
 *  March 12th, Spring 2021
 */

public class Proj3 {

   /**
    * The main method.
    *
    * @param args commandline args
    */
   public static void main(String[] args) {
      Scanner scnr = new Scanner(System.in);

      /*String[] input = {"call", "555555555",  "total", "$25.44", "64"};
      int[] inputHistogram = buildHistogram(input);
      for (int i = 0; i < inputHistogram.length; ++i) {
         if (inputHistogram[i] != 0) {
            System.out.println("" + inputHistogram[i] + " at index " + i);
         }
      }*/

      System.out.print("enter input spam file: ");
      String[] spam = loadWordsFromFile(scnr.nextLine());

      System.out.print("enter input ham file: ");
      String[] ham = loadWordsFromFile(scnr.nextLine());

      System.out.print("enter input stop words file: ");
      String[] stopWords = loadWordsFromFile(scnr.nextLine());

      double[] hamHistogram = generateHistogram(ham, stopWords);
      double[] spamHistogram = generateHistogram(spam, stopWords);
      System.out.println();

      while (true) {
         System.out.println("Enter text message:");
         String sms = scnr.nextLine();
         if (sms.toLowerCase().equals("exit")) {
            System.out.println();
            System.out.println("BYE!");
            System.exit(0);
         }
         System.out.println();

         double c = classifySMS(sms, hamHistogram, spamHistogram, stopWords);

         System.out.println("SCORE: " + c);
         if (c > 0) {
            System.out.println("CLASS: HAM");
         } else {
            System.out.println("class: SPAM");
         }
         System.out.println();
         
      }

   }

   /**
    * 1. This method generates a normalized histogram of given array of words
    * @param words is the array of chosen words to normalize
    * @param stopWords is the array of words to ignore
    * @return the normalied histogram from array of words
    */
   public static double[] generateHistogram(String[] words, 
                                            String[] stopWords) {
      String[] sanitized = getSanitizedWordArray(words, stopWords);
      int[] histogram = buildHistogram(sanitized);
      return normalizeHistogram(histogram);
   }
    
    
   /**
    * This method classifies and SMS, i.e. it determines if a
    * given text message is spam or ham.
    *
    * @param smsText the SMS
    * @param normalizedHamHistogram the normalized histogram of the words 
    * in the ham dataset
    * @param normalizedSpamHistogram the normalized histogram of the words 
    * in the spam dataset
    * @param stopWords a String array with the stop words, loaded from the 
    * file stop_words.txt
    * @return the score of the SMS, it should be positive if it is HAM and 
    * negative if it is SPAM
    */
   public static double classifySMS(String smsText, 
                                    double[] normalizedHamHistogram,
                                    double[] normalizedSpamHistogram,
                                    String[] stopWords) {
      String[] words = smsText.split(" ");
      String[] swords = getSanitizedWordArray(words, stopWords);
      double total = 0.0;
      for (int i = 0; i < swords.length; i++) {
         String word = swords[i];
         int hashCode = getIndex(word);         
         total = total + Math.log(normalizedHamHistogram[hashCode] /
                                  normalizedSpamHistogram[hashCode]);
      }
      return total;
   }

   /**
    * This method normalizes the histogram in such a way that it has unit
    * length.
    * @param histogram the input histogram
    * @return the unit normalized histogram
    */
   public static double[] normalizeHistogram(int[] histogram) {
      double[] output = new double[histogram.length];
      double norm = 0.0;
      for (int i = 0; i < histogram.length; i++) { 
         if (histogram[i] == 0) {
            histogram[i] = 1;
         }
         norm = norm + histogram[i] * histogram[i];
      }
      norm = Math.sqrt(norm);
      for (int i = 0; i < histogram.length; i++) {
         output[i] = histogram[i] / norm;
      }
      return output;
   }

   /**
    * This method receives an array and copies it into another array that
    * is n times larger than the original array.
    * @param input the input String array
    * @param n make the output array n times larger than the input array
    * @return the output, enlarged array
    */
   public static String[] increaseArraySize(String[] input, int n) {
      String[] output = new String[input.length * n];
      // Implement this!
      //System.arraycopy(input, 0 , output, 0, input.length);
      for (int i = 0; i < input.length; ++i) {
         output [i] = input [i];
      }
      return output;
   }

   /**
    * This method receives an array and copies the first size elements into
    * an output array and discards all the elements after size. 
    *
    * @param input the input String array
    * @param size the number of elements to keep
    * @return the output, trimmed array
    */
   public static String[] trimArray(String[] input, int size) {
      String[] output = new String[size];
      // Implement this!
      for (int i = 0; i < size; ++i) {
         output [i] = input [i];
      }
      
      return output;
   }

   /**
    * This method reads the filename line by line and calls split(" ") on 
    * each line. It then includes each word reported by split into an array. 
    * Must return an array with all the words. This method does not know ahead
    * of time how many words are present in the file and may not open and read
    * the file two times.
    *
    * @param filename the filename to load
    * @return a String array with all the words loaded from the file
    */
   public static String[] loadWordsFromFile(String filename) {
      String[] words = new String[100];
      // Implement this!
      try {
         FileInputStream fis = new FileInputStream(filename);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);
         String line;
         int count = 0;
         while ((line = br.readLine()) != null) {
            String[] newWords = line.split(" ");
            for (int j = 0; j < newWords.length; ++j) {
               if (count == words.length) {
                  words = increaseArraySize(words, 2);
               }
               words[count] = newWords[j];
               count += 1;
               
            }
         } 
               
               
      
         //String[] smallerArray = new String[i];
         //System.arraycopy(words, 0 , smallerArray, 0, smallerArray.length);
         //return smallerArray;
         return trimArray(words, count);
      }  
      /*catch (FileNotFoundException e) {
         System.err.print("File missing");
         System.exit(1);
      }*/
      catch (IOException e) {
         System.err.print("Problem reading file");
         System.exit(1);
      }
      return words;
   }


   /**
    * This method removes (trims) all the occurences of trimChars from the 
    * left of the string. For example if trimChars is {'(', '*'}:
    *
    * "(*(*(((75": returns "75"
    * "*(something()something)": returns "something()something)"
    * "99.95": returns "99.95"
    * "((": returns ""
    * "": returns ""
    *
    * Also, don't forget that the implementation fo this method must be
    * recursive.
    *
    * @param word the word
    * @param trimChars the characters to trim
    * @return the left trimmed word
    */
   public static String leftTrim(String word, char[] trimChars) {
      // Implement this!    
      while (word.length() > 0) {
         String original = word;
         if (contains(trimChars, word.charAt(0))) {
            word = word.substring(1, word.length());
         } else {
            break;
         }
      }
      //return leftTrim(word.substring(1, word.length()), trimChars);
      return word;
   }
   
   /**
    * 2. This method finds if char contained is one of the trimChars 
    * @param trimChars the characters to trim
    * @param contained is the character being looked at
    * @return a boolean if contained or not
    */

   public static boolean contains(char[] trimChars, char contained) {
      for (int i = 0; i < trimChars.length; ++i) {
         if (contained == trimChars[i]) {
            return true;
         }
      }
      
      return false;
   }


   /**
    * This method reverses a string.
    *
    * Examples:
    * "Happy": returns "yppaH"
    * "123": returns "321"
    * "radar": returns "radar"
    * "": returns ""
    *
    * @param word the word
    * @return the reversed word
    */
   public static String reverse(String word) {
      // Implement this!
      String reverseWord = "";
      for (int i = 0; i < word.length(); ++i) {
         reverseWord += word.charAt(word.length() - i - 1);
      }
      return reverseWord;
   }


   /**
    * Same as leftTrim but trims on the right of the string. This method 
    * should work without modification.
    *
    * @param word the word
    * @param trimChars the characters to trim
    * @return the right trimmed word
    */
   public static String rightTrim(String word, char[] trimChars) {
      return reverse(leftTrim(reverse(word), trimChars));
      
   }

   /**
    * This method searches for item in the array and returns the index
    * in which it finds it, or -1 if item is not in the array.
    *
    * @param array the array to be searched
    * @param item the item to search for
    * @return the lowest index in which appears in the array or -1 if item is 
    * not in the array
    */
   public static int getArrayIndexForItem(String[] array, String item) {
      // Implement this!
      for (int i = 0; i < array.length; ++i) {
         if (array[i].equals(item)) {
            return i;
         }
      }
      return -1;
   }

   /**
    * This method sanitizes the words array. It goes word by word and left
    * trims the following characters {'.', '(', '"', '\'' }, and right trims
    * the following characters {',', '.', '?', '!', ':', ')', '"', '\'' } from
    * each word. If after trimming the word has length greater than 0 and is
    * not present in the array stopWords then it needs to be included in the
    * return array, otherwise the word is dropped from further consideration.

    * @param words is the input unsanitized array of words
    * @param stopWords is the array with stop words
    * @return the sanitized array constructed as described aboce
    */
   public static String[] getSanitizedWordArray(String[] words, 
                                                 String[] stopWords) {
      String[] output = new String[words.length];
      // Implement this!
      int count = 0;
      char[] left = {'.', '(', '"', '\'' };
      char[] right = {',', '.', '?', '!', ':', ')', '"', '\'' };
      for (int i = 0; i < words.length; ++i) {
         String word = rightTrim(leftTrim(words[i], left), right).toLowerCase();
         if (word.length() > 0 && getArrayIndexForItem(stopWords, word) == -1) {
            output[count] = word;
            count += 1;
         }
      }
      
      return trimArray(output, count);
   }

   /**
    * This method determines if a given word is a number. For the project 
    * we define number as any word that only contains the symbols: '0', '1'
    * '2', '3', '4', '5', '6', '7', '8, '9', '.', '-'
    * Examples:
    * "75": number
    * "75.54": number
    * "$99.95": not number
    * "-33.2": number
    * "45F": not number
    *
    * @param word the word
    * @return true if the input word is an amount of money, false if not
    */
   public static boolean isNumber(String word) {
      // Implement this!
      int decimalCount = 0;
      for (int i = 0; i < word.length(); ++i) {
         if (!(Character.isDigit(word.charAt(i))                 
               || word.charAt(i) == '.'
               || word.charAt(i) == '-')) {
            return false;
         }
         /*
         if (word.charAt(i) == '-' && i != 0) {
            return false;
         }
         
         if (word.charAt(i) == '.') {
            decimalCount += 1;
            if (decimalCount > 1) {
               return false;
            }
         }*/
      }      
      
      return true;
   }
   
   /**
    * This method determines if a given word is an amount of money. For the 
    * project we define money as any word that contains the symbols: 
    * '(BP)' or '$'.
    * Please use '\u00A3' instead of '(BP)' for the autograder to work, this is 
    * limitation of Gradescope.
    * 
    * Examples:
    * "75": not money
    * "75bucks": not money
    * "$99.95": money
    * "$$$": money
    *
    * @param word the word
    * @return true if the input word is an amount of money, false if not
    */
   public static boolean isMoney(String word) {
      // Implement this!
      for (int i = 0; i < word.length(); ++i) {
         if (word.charAt(i) == '$' || word.charAt(i) == '\u00A3') {   
            return true;
         }          
      }      
      return false;
   }

   /**
    * This method determines if a given word is an URL. For the project we 
    * define URL as a word that has at least one dot and no pair of 
    * consecutive dots. Examples:
    * "my...friend": not a URL
    * "my...F.r.i.e.n.d": not a URL
    * "cs.jhu.edu": a URL
    * "GET.OUT.OF.HERE": a URL
    *
    * @param word the word
    * @return true if the input word is a URL, false if not
    */
   public static boolean isURL(String word) {
      // Implement this!
      return word.indexOf(".") != -1
         && word.indexOf("..") == -1;
   }

   /**
    * This method receives a word and computes its non-negative hash code. 
    * Observe that the hash code returned by this method can be from 0 to the 
    * maximum integer in Java, but for a fixed word the hash code is always 
    * the same.
    *
    * @param word the word
    * @return the hashcode
    */
   public static int getHashCode(String word) {
      int hashCode = word.hashCode() & 0xfffffff;
      return hashCode;
   }

   /**
    * This method receives an array of words (strings) and builds a histogram.
    * The histogram is 1000-dimensional and will be structured as follows:
    *
    * - index 0 contains the count of words in the array for which isNumber is
    * true
    * - index 1 contains the count of words in the array for which isMoney is
    * true
    * - index 2 contains the count of words in the array for which isURL is
    * true
    * - from index 3 to index 999 if the word is not a number, money or URL, 
    * then a hash code of the word (from 0 to 996 will) be computed and will 
    * be counted in index 3 to 999 of the histogram
    *
    * @param words is the array of words
    * @return will return a 1000-dimensional histogram as described above
    */
   public static int[] buildHistogram(String[] words) {
      int[] histogram = new int[1000];
      // Implement this!      
      
      for (int i = 0; i < words.length; ++i) {
         histogram[getIndex(words[i])] += 1;
      }
      for (int i = 0; i < histogram.length; ++i) {
         if (histogram[i] == 0) {
            histogram[i] = 1;
         }
      }
      return histogram;
   }
   
   /**
    * 3. This method receives a word (string) and returns standardized index.
    * @param word is string to be classified into array index
    * @return # is appropiate index to be assigned to word
    */

   public static int getIndex(String word) {
      if (isNumber(word)) {
         return 0;
      }
      if (isMoney(word)) {
         return 1;
      }
      if (isURL(word)) {
         return 2;
      } 
      return 3 + (getHashCode(word) % 997);
   }
}