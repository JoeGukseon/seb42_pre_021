import TextEditor from 'components/Editor';
import HowToEdit from 'components/HowToEdit';
import HowToFormat from 'components/HowToFormat';
import HowToTag from 'components/HowToTag';
import { Container } from 'containers/Container';
import Navigation from 'containers/Navigation';
import { useEffect, useRef, useState } from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

const QuestionEdit = () => {
  const location = useLocation();
  const { content } = location.state;
  const [currentForm, setCurrentForm] = useState('edit');
  const [isChanged, setIsChanged] = useState(false);
  const questionEditRef = useRef('');

  useEffect(() => {}, []);

  const handleSectionClick = form => {
    setCurrentForm(form);
  };

  const handleEditorChange = () => {
    const ref = questionEditRef.current?.getInstance().getMarkdown();
    if (ref === content.markdown) {
      setIsChanged(false);
    } else {
      setIsChanged(true);
    }
  };

  return (
    <Container>
      <Navigation />
      <Wrapper>
        <EditSection>
          <TopNotice>
            Your edit will be placed in a queue until it is peer reviewed.
            <br />
            <br />
            We welcome edits that make the post easier to understand and more valuable for readers.
            Because community members review edits, please try to make the post substantially better
            than how you found it, for example, by fixing grammar or adding additional resources and
            hyperlinks.
          </TopNotice>
          <TitleEdit onClick={() => handleSectionClick('edit')}>
            <InputTitle>Title</InputTitle>
            <input type="text" />
          </TitleEdit>
          <BodyEdit onClick={() => handleSectionClick('format')}>
            <InputTitle>Body</InputTitle>
            <div
              className={
                currentForm === 'format'
                  ? `focused ${isChanged ? 'changed' : 'not_changed'}`
                  : `not_focused ${isChanged ? null : 'not_changed'}`
              }
            >
              <TextEditor
                editorRef={questionEditRef}
                editorValue={content.markdown}
                editorHeight="400px"
                onEditorChange={handleEditorChange}
              />
              {!isChanged ? (
                <span>It looks like your post is not changed; please add some more details.</span>
              ) : null}
            </div>
          </BodyEdit>
          <TagEdit onClick={() => handleSectionClick('tag')}>
            <InputTitle>Tags</InputTitle>
          </TagEdit>
        </EditSection>
        <SideNotice>
          {currentForm === 'edit' ? (
            <HowToEdit />
          ) : currentForm === 'format' ? (
            <HowToFormat />
          ) : (
            <HowToTag />
          )}
        </SideNotice>
      </Wrapper>
    </Container>
  );
};

const Wrapper = styled.div`
  width: 100%;
  height: max-content;
  display: flex;
  padding: 1.7rem 0;
  @media screen and (max-width: 1279px) {
    display: grid;
    grid-template-columns: calc(100% - 24rem) 23rem;
    padding-right: 1rem;
  }
  @media screen and (max-width: 1049px) {
    grid-template-columns: 100%;
    padding: 1.7rem 1rem;
  }
`;

const EditSection = styled.section`
  width: 44rem;
  padding: 0 1.7rem;
  @media screen and (max-width: 1279px) {
    width: 100%;
  }
  @media screen and (max-width: 979px) {
    padding: 0;
  }
`;

const TopNotice = styled.div`
  width: 100%;
  background-color: #fdf7e2;
  padding: 1rem;
  font-size: 0.8rem;
  border-radius: 5px;
  border: 1px solid #f1e5bc;
`;

const SideNotice = styled.aside`
  background-color: #fdf7e2;
  height: fit-content;
  width: 23rem;
  position: sticky;
  top: 4.1rem;
  border-radius: 5px;
  border: 1px solid #f1e5bc;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  > div {
    padding: 0.8rem;
    width: 100%;
    background-color: #fbf3d5;
    border-bottom: 1px solid #f1e5bc;
  }
  > ul {
    padding: 1rem 0.8rem;
    padding-left: 1.8rem;
    > li {
      font-size: 0.8rem;
      margin-bottom: 0.5rem;
      :last-of-type {
        margin-bottom: 0;
      }
    }
    .style_none {
      list-style: none;
      margin-left: -0.7rem;
    }
  }
  @media screen and (max-width: 1279px) {
    width: 100%;
  }
  @media screen and (max-width: 979px) {
    position: relative;
    top: auto;
  }
`;

const InputTitle = styled.h1`
  font-size: 1rem;
  margin-top: 1rem;
  margin-bottom: 0.3rem;
  cursor: pointer;
`;

const TitleEdit = styled.div`
  width: 100%;
  > input {
    width: 100%;
    border: 1px solid #bbb;
    border-radius: 3px;
    padding: 0.3rem 0.5rem;
    :focus {
      border: 1px solid #58a4de;
      outline: 4px solid #ddeaf7;
    }
  }
`;

const BodyEdit = styled.div`
  width: 100%;
  position: relative;
  .toastui-editor-toolbar {
    overflow: hidden;
  }
  .focused.changed {
    .toastui-editor-main {
      border-radius: 3px;
      border: 1px solid #58a4de;
      outline: 4px solid #ddeaf7;
    }
  }
  .focused.not_changed {
    .toastui-editor-main {
      border-radius: 3px;
      border: 1px solid red;
      outline: 4px solid #f8e1e0;
    }
    > span {
      font-size: 0.8rem;
      margin-top: 1rem;
      color: #d0393e;
    }
  }
  .not_focused.not_changed {
    .toastui-editor-main {
      border: 1px solid red;
      border-radius: 0px 0px 3px 3px;
      outline: none;
    }
    > span {
      font-size: 0.8rem;
      margin-top: 1rem;
      color: #d0393e;
    }
  }
`;

const TagEdit = styled.div`
  width: 100%;
`;

export default QuestionEdit;
