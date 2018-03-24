package com.github.grishberg.tests.planner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Holder for instrumental test.
 */
public class InstrumentalTestHolder {
    private List<TestPlanElement> planList;
    private final PackageTreeGenerator packageTreeGenerator;
    private ArrayList<TestPlanElement> prevRoots = new ArrayList<>();

    InstrumentalTestHolder(List<TestPlanElement> planList, PackageTreeGenerator packageTreeGenerator) {
        this.planList = planList;

        this.packageTreeGenerator = packageTreeGenerator;
    }

    /**
     * @return iterator with all test methods in project.
     */
    public Iterator<TestPlanElement> provideTestNodeElementsIterator() {
        prevRoots.clear();
        prevRoots.addAll(packageTreeGenerator.makePackageTree(planList));
        return new FlatIterator(prevRoots);
    }

    public List<TestPlanElement> provideCompoundTestPlan() {
        ArrayList<TestPlanElement> compoundPlans = new ArrayList<>();
        for (TestPlanElement rootElement : prevRoots) {
            List<TestPlanElement> compoundElements = rootElement.getCompoundElements();
            for (TestPlanElement currentCompoundElement : compoundElements) {
                compoundPlans.add(currentCompoundElement);
            }
        }
        return compoundPlans;
    }

    /**
     * Returns tree-items in flat list.
     */
    private static class FlatIterator implements Iterator<TestPlanElement> {
        private final Iterator<TestPlanElement> iterator;

        FlatIterator(ArrayList<TestPlanElement> roots) {
            ArrayList<TestPlanElement> flatList = new ArrayList<>();
            for (TestPlanElement rootElement : roots) {
                flatList.addAll(rootElement.getAllTestMethods());
            }
            iterator = flatList.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public TestPlanElement next() {
            return iterator.next();
        }
    }
}
