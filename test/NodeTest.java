import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    //testing if two hashes with equal input are equal
    @org.junit.Test
    public void hashesAreTheSame() {
        MerkleHash h1 = MerkleHash.create("hello");
        MerkleHash h2 = MerkleHash.create("hello");
        assertTrue(h1.equals(h2));
    }

    //Testing if a node can be created
    @org.junit.Test
    public void createNode() {
        MerkleNode node = new MerkleNode();
        assertNull(node.getParent());
        assertNull(node.getLeftNode());
        assertNull(node.getRightNode());
    }

    //Testing if a parent node is successfully created from
    // one left child
    @org.junit.Test
    public void leftHashVerification() {
        MerkleNode parent = new MerkleNode();
        MerkleNode leftChild = new MerkleNode(MerkleHash.create("hello"));
        parent.setLeftNode(leftChild);
        assertTrue(parent.verifyHash());
    }

    //Testing if a parent node is succesfuslly create from
    // two children Left and Right
    @org.junit.Test
    public void leftRightHashVerification() {
        MerkleNode parentNode = createParentNode("foo", "bar");
        assertTrue(parentNode.verifyHash());
    }

    //Testing if a two same nodes are equal
    @org.junit.Test
    public void nodesEqual() {
        MerkleNode n1 = createParentNode("foo", "bar");
        MerkleNode n2 = createParentNode("foo", "bar");
        assertTrue(n1.equals(n2));
    }

    //Testing if a two different nodes are different
    @org.junit.Test
    public void nodesNotEqual() {
        MerkleNode n1 = createParentNode("foo", "bar");
        MerkleNode n2 = createParentNode("pew", "bar");
        assertFalse(n1.equals(n2));
    }

    //Testing if a tree levels
    @org.junit.Test
    public void verifyTwoLevelTree() {
        MerkleNode parentNode1 = createParentNode("foo", "bar");
        MerkleNode parentNode2 = createParentNode("pew", "bar");
        MerkleNode rootNode = new MerkleNode();
        rootNode.setLeftNode(parentNode1);
        rootNode.setRightNode(parentNode2);
        assertTrue(rootNode.verifyHash());
    }

    //Testing if a tree with even leafs can be built
    @org.junit.Test
    public void createBalancedTree() {
        MerkleTree tree = new MerkleTree();
        tree.appendLeaf(MerkleHash.create("abc"));
        tree.appendLeaf(MerkleHash.create("def"));
        tree.appendLeaf(MerkleHash.create("ghi"));
        tree.appendLeaf(MerkleHash.create("jkl"));
        tree.buildTree();
        assertNotNull(tree.getRoot());
    }

    //Testing if a tree with uneven leafs can be built
    @org.junit.Test
    public void createUnbalancedTree() {
        MerkleTree tree = new MerkleTree();
        tree.appendLeaf(MerkleHash.create("abc"));
        tree.appendLeaf(MerkleHash.create("def"));
        tree.appendLeaf(MerkleHash.create("ghi"));
        tree.buildTree();
        assertNotNull(tree.getRoot());
    }

    //Testing if it can be verified, that a leaf is part of a Tree
    @org.junit.Test
    public void auditTest() {
        MerkleTree tree = new MerkleTree();
        MerkleHash l1 = MerkleHash.create("abc");
        MerkleHash l2 = MerkleHash.create("def");
        MerkleHash l3 = MerkleHash.create("ghi");
        MerkleHash l4 = MerkleHash.create("jkl");
        tree.appendLeaves(new MerkleHash[]{l1, l2, l3, l4});
        MerkleHash rootHash = tree.buildTree();

        List<MerkleProofHash> auditTrail = tree.auditProof(l1);
        assertTrue(MerkleTree.verifyAudit(rootHash, l1, auditTrail));

        auditTrail = tree.auditProof(l2);
        assertTrue(MerkleTree.verifyAudit(rootHash, l2, auditTrail));

        auditTrail = tree.auditProof(l3);
        assertTrue(MerkleTree.verifyAudit(rootHash, l3, auditTrail));

        auditTrail = tree.auditProof(l4);
        assertTrue(MerkleTree.verifyAudit(rootHash, l4, auditTrail));
    }

    //Testing if 2 trees can be merged successfully
    @org.junit.Test
    public void addTreeTest(){

        // Create first Tree with 4 leaves
        MerkleTree tree1 = new MerkleTree();
        MerkleHash l1 = MerkleHash.create("abc");
        MerkleHash l2 = MerkleHash.create("def");
        MerkleHash l3 = MerkleHash.create("ghi");
        MerkleHash l4 = MerkleHash.create("jkl");
        tree1.appendLeaves(new MerkleHash[]{l1, l2, l3, l4});
        MerkleHash rootHash1 = tree1.buildTree();

        // Create second Tree with 3 leaves
        MerkleTree tree2 = new MerkleTree();
        MerkleHash l5 = MerkleHash.create("123");
        MerkleHash l6 = MerkleHash.create("456");
        MerkleHash l7 = MerkleHash.create("789");
        tree2.appendLeaves(new MerkleHash[]{l5, l6, l7});
        MerkleHash rootHash2 = tree2.buildTree();

        // Merge the two trees
        // tree1 is the merged tree
        // Assert that the old root is different from the new
        MerkleHash rootHashAfterAddTree = tree1.addTree(tree2);
        assertFalse(rootHash1.equals(rootHashAfterAddTree));

        //Assert that a leaf from the second tree can be verified
        // in the newly merged tree.
        List<MerkleProofHash> auditTrail = tree1.auditProof(l7);
        assertTrue(MerkleTree.verifyAudit(rootHashAfterAddTree, l7, auditTrail));
    }

    //Helper Function to create a parent MerkleNode from two Strings
    private MerkleNode createParentNode(String leftData, String rightData) {
        MerkleNode parentNode = new MerkleNode();
        MerkleNode leftNode = new MerkleNode(MerkleHash.create(leftData));
        MerkleNode rightNode = new MerkleNode(MerkleHash.create(rightData));

        parentNode.setLeftNode(leftNode);
        parentNode.setRightNode(rightNode);

        return parentNode;
    }
}