package edu.byu.cs.tweeter.shared.service.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.shared.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.service.request.FollowersRequest}.
 */
public class FollowersResponse extends PagedResponse {

    private List<User> followers;

    public FollowersResponse() {
        super();
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowersResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param followers the followers to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public FollowersResponse(List<User> followers, boolean hasMorePages) {
        super(true, hasMorePages);
        this.followers = followers;
    }

    /**
     * Returns the followers for the corresponding request.
     *
     * @return the followers.
     */
    public List<User> getFollowers() {
        return followers;
    }

    public boolean getSuccess() {
        return super.isSuccess();
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowersResponse that = (FollowersResponse) o;
        return Objects.equals(followers, that.followers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followers);
    }
}
