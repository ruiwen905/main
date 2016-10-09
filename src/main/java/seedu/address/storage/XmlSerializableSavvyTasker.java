package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.ReadOnlySavvyTasker;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.TaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable SavvyTasker that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableSavvyTasker implements ReadOnlySavvyTasker {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    @XmlElement
    private List<Tag> tags;

    {
        tasks = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableSavvyTasker() {}

    /**
     * Conversion
     */
    public XmlSerializableSavvyTasker(ReadOnlySavvyTasker src) {
        tasks.addAll(src.getReadOnlyListOfTasks().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
    }

    @Override
    public TaskList getTaskList() {
        TaskList lists = new TaskList();
        for (XmlAdaptedTask t : tasks) {
            try {
                lists.add(t.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getReadOnlyListOfTasks() {
        return tasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
