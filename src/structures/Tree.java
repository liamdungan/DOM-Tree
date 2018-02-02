package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		
		//create stack
		Stack<TagNode> tagStack = new Stack<TagNode>();
		String curr = sc.nextLine();
		//create root and push into stack
		root = new TagNode("html", null, null);
		tagStack.push(root);

		while (sc.hasNextLine()){
			curr = sc.nextLine();

			if( curr.contains("/") && curr.contains("<") && curr.contains(">") ){
				tagStack.pop();
			}else if( !curr.contains("/") && curr.contains("<") && curr.contains(">") ){
				
				if(tagStack.peek().firstChild == null){
						TagNode buildNode = new TagNode(curr.replace("<", "").replace(">", ""), null, null);
						tagStack.peek().firstChild = buildNode;
						tagStack.push(buildNode);
					
				}else{
						TagNode thisTag = tagStack.peek().firstChild;
						while (thisTag.sibling != null){
							thisTag = thisTag.sibling;
						}
	
						TagNode buildNode = new TagNode(curr.replace("<", "").replace(">", ""), null, null);
						thisTag.sibling = buildNode;
						tagStack.push(buildNode);
				}
			}else{
				if (tagStack.peek().firstChild == null){
						tagStack.peek().firstChild = new TagNode(curr, null, null);
				}else{
						TagNode thisTag = tagStack.peek().firstChild;
						while (thisTag.sibling != null){
							thisTag = thisTag.sibling;
						}
	
						
						thisTag.sibling = new TagNode(curr, null, null);
				}
			}
		}
	}
	
	
	
	
	
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		// HELPER METHOD BELOW
		if (root == null || oldTag == null || newTag == null){
			return;
		}else{
			replaceTag_Helper(oldTag, newTag, root.firstChild);
		}
	}
	//helper method for replaceTag
	private void replaceTag_Helper(String oldTag, String newTag, TagNode tempNode) {
		//base
		if (tempNode == null){
			return;
		}else if (tempNode.tag.compareTo(oldTag) == 0){
			tempNode.tag = newTag;
		}
		//recurse
		replaceTag_Helper(oldTag, newTag, tempNode.firstChild);
		replaceTag_Helper(oldTag, newTag, tempNode.sibling);
	}
	
	
	
	
	
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		//HELPER METHOD BELOW
		if(row <= 0){
			return;
		}else{
			boldRow_Helper(row, 0, root, root.firstChild);
		}
	}
	//helper method for boldRow
	private void boldRow_Helper(int row, int counter, TagNode prevNode, TagNode tempNode) {
		
		if(tempNode == null){
			return;
		}else if(tempNode.tag.equals("tr")){
			counter++;
		}if(counter == row && tempNode.firstChild == null){
			prevNode.firstChild = new TagNode("b", tempNode, null);
		} 
		//recurse
		boldRow_Helper(row, counter, tempNode, tempNode.firstChild); 
		boldRow_Helper(row, counter, tempNode, tempNode.sibling);
	}
	
	
	
	
	
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		//HELPER METHODS BELOW
		if (root == null){
			return;
		}
		else{
			while (hasTag(tag, root)){
				removeTag_Helper(tag, root, root.firstChild);
			}
		}
	}
	//helper method 1
	private void removeTag_Helper(String tag, TagNode prevNode, TagNode tempNode) {
		
		if (tempNode == null || prevNode == null){
			return;
			
		}else if(tempNode.tag.equals(tag)){

					if(tag.equals("ul") || tag.equals("ol"))
						removeTag_Helper(tempNode.firstChild); 
					
					if(prevNode.firstChild == tempNode){
						prevNode.firstChild = tempNode.firstChild;
						addLastSib_Helper(tempNode.firstChild, tempNode.sibling);	
						
					}else if(prevNode.sibling == tempNode){
						addLastSib_Helper(tempNode.firstChild, tempNode.sibling);
						prevNode.sibling = tempNode.firstChild;
						
					}
					return;
		}

		prevNode = tempNode;
		removeTag_Helper(tag, prevNode, tempNode.firstChild);
		removeTag_Helper(tag, prevNode, tempNode.sibling);
	}
	//helper method 2
	private boolean hasTag(String tag, TagNode tempNode) {
		if (tempNode == null){
			return false;
		}else if (tempNode.tag.compareTo(tag) == 0){
			return true;
		}

		return hasTag(tag, tempNode.firstChild) || hasTag(tag, tempNode.sibling);
	}
	//helper method 3
	private TagNode getlastSib_Helper(TagNode tempNode) {
		while (tempNode.sibling != null){
			tempNode = tempNode.sibling;
		}
		return tempNode;
	}
	//helper method 4
	private void addLastSib_Helper(TagNode tempNode, TagNode newSibling) {
		tempNode = getlastSib_Helper(tempNode);
		tempNode.sibling = newSibling;
	}
	//helper method 5
	private void removeTag_Helper(TagNode tempNode) {
		if(tempNode == null){
			return;
			
		}else if(tempNode.tag.compareTo("li") == 0){
			tempNode.tag = "p";
			
		}

		removeTag_Helper(tempNode.sibling);
	}

	
	
	
	
	
	
	
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		//HELPER METHODS BELOW
		if(word == null || tag == null || root == null)
			return;
		
		addTag_Helper(word, tag, root.firstChild);
	}
	//helper method 1
	private void addTag_Helper(String word, String tag, TagNode tempNode) {
		
		if(tempNode == null)
			return;
		else if(tempNode.tag.toLowerCase().contains(word.toLowerCase()) ){			
			if(tempNode.tag.equalsIgnoreCase(word)){
				tempNode.tag = tag;
				tempNode.firstChild = new TagNode (word, tempNode.firstChild, null);
			}else if(tempNode.tag.toLowerCase().contains(word.toLowerCase())){	
				TagNode sibling = tempNode.sibling;
				String before = tempNode.tag.substring(0, tempNode.tag.toLowerCase().indexOf(word.toLowerCase()) );
				String after = tempNode.tag.substring(tempNode.tag.toLowerCase().indexOf(word.toLowerCase()) + word.length());
				String punc = "";
				String original = tempNode.tag.substring(tempNode.tag.toLowerCase().indexOf(word.toLowerCase()), tempNode.tag.toLowerCase().indexOf(word.toLowerCase()) + word.length());

				if(after.length()>0 ){
							if (after.length() > 1 && (punctCheck(after.charAt(0)) && !punctCheck(after.charAt(1))) ){
								punc = "" + after.charAt(0);
								after = after.substring(1);
							}
				}
				
				if(after.length() == 0 || (after.length() >= 1 && (after.charAt(0) == ' ' || punctCheck(after.charAt(0)))) ){
							if(after.equals(",") || after.equals(".") || after.equals("!") || after.equals("?")){
								original = original + after;
								after = "";
							}
							tempNode.tag = before;
							tempNode.sibling = new TagNode(tag, new TagNode(original + punc, null, null), null);
							
							if(after.length() > 0){
								if (sibling != null){
									tempNode.sibling.sibling = new TagNode(after, null, sibling);
								}else{
									tempNode.sibling.sibling = new TagNode(after, null, null);
								}
							}else if(sibling != null){
								tempNode.sibling.sibling = sibling;
							} 
				} 
			}
			addTag_Helper(word, tag, tempNode.sibling.sibling);
			
		}else{
			
			
			addTag_Helper(word, tag, tempNode.firstChild);
			addTag_Helper(word, tag, tempNode.sibling);
		}
		
	}
	//helper method 2
	private boolean punctCheck(char c) {
		if(c == '.' || c == ',' || c == '?' || c == '!')
			return true;
		
		return false;
	}

	
	
	
	
	
	
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}
