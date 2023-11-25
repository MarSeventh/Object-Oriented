import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;
import java.util.List;

public class Query {
    private final Network network;

    public Query(Network network) {
        this.network = network;
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (network.contains(id1) && network.contains(id2) &&
                network.getPerson(id1).isLinked(network.getPerson(id2))) {
            return network.getPerson(id1).queryValue(network.getPerson(id2));
        } else {
            if (!network.contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!network.contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (((MyNetwork) network).getGroups().containsKey(id)) {
            return network.getGroup(id).getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (((MyNetwork) network).getGroups().containsKey(id)) {
            return network.getGroup(id).getAgeVar();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (network.contains(id)) {
            return network.getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (network.contains(id)) {
            return network.getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (network.contains(id)) {
            return network.getPerson(id).getMoney();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        if (network.contains(id)) {
            HashMap<Integer, Person> map = new HashMap<>();
            for (HashMap<Integer, Person> block : ((MyNetwork) network).getBlocks()) {
                if (block.containsKey(id)) {
                    map = block;
                    break;
                }
            }
            int leastmoments = ((MyPerson) network.getPerson(id)).getLeastMoments(map);
            if (leastmoments == -1) {
                throw new MyPathNotFoundException(id);
            } else {
                return leastmoments;
            }
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (network.contains(id)) {
            MyPerson myperson = (MyPerson) network.getPerson(id);
            int bestid = myperson.getBestAcquaintance();
            if (myperson.getAcqArray().size() > 0) {
                return bestid;
            } else {
                throw new MyAcquaintanceNotFoundException(id);
            }
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = network.getMessage(id);
        if (network.containsMessage(id) && message.getType() == 0
                && message.getPerson1().isLinked(message.getPerson2()) &&
                !message.getPerson1().equals(message.getPerson2())) {
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();
            person1.addSocialValue(message.getSocialValue());
            person2.addSocialValue(message.getSocialValue());
            ((MyNetwork) network).getMessages().remove(id);
            person2.getMessages().add(0, message);
            if (message instanceof RedEnvelopeMessage) {
                person1.addMoney(-((RedEnvelopeMessage) message).getMoney());
                person2.addMoney(((RedEnvelopeMessage) message).getMoney());
            }
            if (message instanceof EmojiMessage) {
                ((MyNetwork) network).getEmojiList().merge(((EmojiMessage) message).getEmojiId(),
                        1, Integer::sum);
            }
        } else if (network.containsMessage(id) && message.getType() == 1 &&
                message.getGroup().hasPerson(message.getPerson1())) {
            MyGroup mygroup = (MyGroup) message.getGroup();
            mygroup.addSocialValue(message.getSocialValue());
            if (message instanceof RedEnvelopeMessage) {
                mygroup.sendRedEnvelope((RedEnvelopeMessage) message);
            }
            if (message instanceof EmojiMessage) {
                ((MyNetwork) network).getEmojiList().merge(((EmojiMessage) message).getEmojiId(),
                        1, Integer::sum);
            }
            ((MyNetwork) network).getMessages().remove(id);
        } else {
            if (!network.containsMessage(id)) {
                throw new MyMessageIdNotFoundException(id);
            } else {
                if (message.getType() == 0 &&
                        !message.getPerson1().isLinked(message.getPerson2())) {
                    throw new MyRelationNotFoundException(message.getPerson1().getId(),
                            message.getPerson2().getId());
                }
                if (message.getType() == 1 &&
                        !message.getGroup().hasPerson(message.getPerson1())) {
                    throw new MyPersonIdNotFoundException(message.getPerson1().getId());
                }
            }
        }
    }
}
