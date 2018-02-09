import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @org.junit.Test
    public void hashesAreTheSame() {
        MerkleHash h1 = MerkleHash.create("hello");
        MerkleHash h2 = MerkleHash.create("hello");
        assertTrue(h1.equals(h2));
    }

    @org.junit.Test
    public void createNode() {
        MerkleNode node = new MerkleNode();
        assertNull(node.getParent());
        assertNull(node.getLeftNode());
        assertNull(node.getRightNode());
    }

    @org.junit.Test
    public void leftHashVerification() {
        MerkleNode parent = new MerkleNode();
        MerkleNode leftChild = new MerkleNode(MerkleHash.create("hello"));
        parent.setLeftNode(leftChild);
        assertTrue(parent.verifyHash());
    }

    @org.junit.Test
    public void leftRightHashVerification() {
        MerkleNode parentNode = createParentNode("foo", "bar");
        assertTrue(parentNode.verifyHash());
    }

    @org.junit.Test
    public void nodesEqual() {
        MerkleNode n1 = createParentNode("foo", "bar");
        MerkleNode n2 = createParentNode("foo", "bar");
        assertTrue(n1.equals(n2));
    }

    @org.junit.Test
    public void nodesNotEqual() {
        MerkleNode n1 = createParentNode("foo", "bar");
        MerkleNode n2 = createParentNode("pew", "bar");
        assertFalse(n1.equals(n2));
    }

    @org.junit.Test
    public void verifyTwoLevelTree() {
        MerkleNode parentNode1 = createParentNode("foo", "bar");
        MerkleNode parentNode2 = createParentNode("pew", "bar");
        MerkleNode rootNode = new MerkleNode();
        rootNode.setLeftNode(parentNode1);
        rootNode.setRightNode(parentNode2);
        assertTrue(rootNode.verifyHash());
    }

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

    @org.junit.Test
    public void createUnbalancedTree() {
        MerkleTree tree = new MerkleTree();
        tree.appendLeaf(MerkleHash.create("abc"));
        tree.appendLeaf(MerkleHash.create("def"));
        tree.appendLeaf(MerkleHash.create("ghi"));
        tree.buildTree();
        assertNotNull(tree.getRoot());
    }

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

    @org.junit.Test
    public void addTreeTest(){

        MerkleTree tree1 = new MerkleTree();
        MerkleHash l1 = MerkleHash.create("abc");
        MerkleHash l2 = MerkleHash.create("def");
        MerkleHash l3 = MerkleHash.create("ghi");
        MerkleHash l4 = MerkleHash.create("jkl");
        tree1.appendLeaves(new MerkleHash[]{l1, l2, l3, l4});
        MerkleHash rootHash1 = tree1.buildTree();

        MerkleTree tree2 = new MerkleTree();
        MerkleHash l5 = MerkleHash.create("123");
        MerkleHash l6 = MerkleHash.create("456");
        MerkleHash l7 = MerkleHash.create("789");
        tree2.appendLeaves(new MerkleHash[]{l5, l6, l7});
        MerkleHash rootHash2 = tree2.buildTree();

        MerkleHash rootHashAfterAddTree = tree1.addTree(tree2);

        assertFalse(rootHash1.equals(rootHashAfterAddTree));

    }

    private MerkleNode createParentNode(String leftData, String rightData) {
        MerkleNode parentNode = new MerkleNode();
        MerkleNode leftNode = new MerkleNode(MerkleHash.create(leftData));
        MerkleNode rightNode = new MerkleNode(MerkleHash.create(rightData));

        parentNode.setLeftNode(leftNode);
        parentNode.setRightNode(rightNode);

        return parentNode;
    }

}