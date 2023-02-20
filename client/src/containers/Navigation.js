import { Link, NavLink } from 'react-router-dom';
import styled from 'styled-components';
import { ReactComponent as Earth } from 'assets/earthvector.svg';

const Navigation = () => {
  return (
    <NavContainer>
      <NavUl>
        <li>
          <Link to="/" className="homeLink">
            <NavSpan>Home</NavSpan>
          </Link>
        </li>
      </NavUl>
      <NavUl>
        <NavSpan>PUBLIC</NavSpan>
        <NavUl>
          <li>
            <StyleLink to="/">
              <Earth className="earth" />
              <PublicSpan>Questions</PublicSpan>
            </StyleLink>
          </li>
          <li>
            <StyleLink to="/Tags">
              <PublicSpan>Tags</PublicSpan>
            </StyleLink>
          </li>
          <li>
            <StyleLink to="/Users">
              <PublicSpan>Users</PublicSpan>
            </StyleLink>
          </li>
          <li>
            <StyleLink to="/Companies">
              <PublicSpan>Companies</PublicSpan>
            </StyleLink>
          </li>
        </NavUl>
      </NavUl>
    </NavContainer>
  );
};

const NavContainer = styled.nav`
  display: flex;
  height: 100%;
  width: 11rem;
  padding-top: 1rem;
  border-right: 1px solid #bbc0c4;
  flex-direction: column;
  z-index: 1;
`;

const NavUl = styled.ul`
  width: 100%;
  list-style: none;
  > li {
    display: block;
    > .homeLink {
      display: block;
      text-decoration: none;
      font-size: 0.8rem;
      color: #666;
    }
  }
`;

const NavSpan = styled.span`
  display: flex;
  font-size: 0.8rem;
  color: #666;
  padding: 0.7rem 0;
`;

const StyleLink = styled(NavLink)`
  display: block;
  text-decoration: none;
  font-size: 0.8rem;
  color: #666;
  width: 100%;
  padding: 0.5rem 0 0.5rem 0.5rem;

  &.active {
    background-color: rgb(241, 242, 243);
    border-right: 2px solid #f48024;
  }

  > .earth {
    width: 1rem;
    position: absolute;
  }
`;

const PublicSpan = styled.span`
  margin-left: 1.5rem;
`;

export default Navigation;
