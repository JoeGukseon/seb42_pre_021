import styled from 'styled-components';

const EditAndProfile = ({ member, asked, isAnswer }) => {
  return (
    <ProfileWrapper>
      <EditAndDelete>
        <li>Edit</li>
        <li>Delete</li>
      </EditAndDelete>
      <Profile className={isAnswer ? 'answer_profile' : null}>
        <img src={member.profile} alt={`${member.nickname} profile`} />
        <div>
          <p>{member.nickname}</p>
          <p>
            {isAnswer ? 'answered' : 'asked'} {new Date(asked).toLocaleDateString()}
          </p>
        </div>
      </Profile>
    </ProfileWrapper>
  );
};

const ProfileWrapper = styled.div`
  width: 100%;
  margin-top: 3rem;
  display: flex;
  justify-content: space-between;
  .answer_profile {
    background-color: #fff;
  }
  @media screen and (max-width: 640px) {
    flex-direction: column;
    gap: 1rem;
  }
`;

const EditAndDelete = styled.ul`
  list-style: none;
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
  color: #888;
  font-weight: 600;
  cursor: pointer;
  li {
    :hover {
      color: #aaa;
    }
  }
`;

const Profile = styled.div`
  width: 13rem;
  height: 4.3rem;
  background-color: #d0e2f0;
  padding: 0.3rem 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0.5rem;
  > img {
    width: 2.5rem;
    margin-right: 1rem;
    cursor: pointer;
  }
  > div {
    display: flex;
    flex-direction: column;
    > p {
      :first-of-type {
        cursor: pointer;
      }
      :last-of-type {
        margin-top: 0.2rem;
        color: #777;
        font-size: 0.8rem;
      }
    }
  }
  @media screen and (max-width: 640px) {
    align-self: flex-end;
  }
`;

export default EditAndProfile;
