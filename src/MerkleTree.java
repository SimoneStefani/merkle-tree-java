import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    private MerkleNode root;
    private List<MerkleNode> nodes;
    private List<MerkleNode> leaves;

    public MerkleTree() {
        this.nodes = new ArrayList<>();
        this.leaves = new ArrayList<>();
    }

    public MerkleNode appendLeaf(MerkleNode node) {
        this.nodes.add(node);
        this.leaves.add(node);
        return node;
    }

    public void appendLeaves(MerkleNode[] nodes) {
        for (MerkleNode node : nodes) {
            this.appendLeaf(node);
        }
    }

    public MerkleNode appendLeaf(MerkleHash hash) {
        return this.appendLeaf(new MerkleNode(hash));
    }

    public List<MerkleNode> appendLeaves(MerkleHash[] hashes) {
        List<MerkleNode> nodes = new ArrayList<>();
        for (MerkleHash hash : hashes) {
            nodes.add(this.appendLeaf(hash));
        }
        return nodes;
    }

    public MerkleHash addTree(MerkleTree tree) {
        if (this.leaves.size() <= 0) throw new InvalidParameterException("Cannot add to a tree with no leaves!");
        tree.leaves.forEach(this::appendLeaf);
        return this.buildTree();
    }

    public MerkleHash buildTree() {
        if (this.leaves.size() <= 0) throw new InvalidParameterException("Cannot add to a tree with no leaves!");
        this.buildTree(this.leaves);
        return this.root.getHash();
    }

    public void buildTree(List<MerkleNode> nodes) {
        if (nodes.size() <= 0) throw new InvalidParameterException("Node list not expected to be empty!");

        if (nodes.size() == 1) {
            this.root = nodes.get(0);
        } else {
            List<MerkleNode> parents = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                MerkleNode right = (i + 1 < nodes.size()) ? nodes.get(i + 1) : null;
                MerkleNode parent = new MerkleNode(nodes.get(i), right);
                parents.add(parent);
            }
            buildTree(parents);
        }
    }
}
