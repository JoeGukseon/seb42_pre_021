import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import Navigation from 'containers/Navigation';
import SideContent from 'components/SideContent';
import DetailTitle from 'components/DetailTitle';
import Vote from 'components/Vote';
import Answers from 'components/Answers';
import MarkdownContent from 'components/MarkdownContent';
import YourAnswer from 'components/YourAnswer';
import { Container } from 'containers/Container';
import baseURL from 'api/baseURL';

const QuestionDetail = () => {
  const { id } = useParams();
  const [question, setQuestion] = useState({});

  const getQuestionData = async () => {
    await baseURL.get(`/questions/${id}`).then(response => setQuestion(response.data));
  };

  useEffect(() => {
    window.scrollTo({
      top: 0,
    });
  }, [window.scrollY]);

  useEffect(() => {
    getQuestionData();
  }, []);

  return (
    <>
      {question.questionId && (
        <>
          <Container>
            <Navigation />
            <DetailTitle question={question} />
            <ContentSection>
              <Wrapper>
                <div className="question_content">
                  <Vote />
                  <MarkdownContent data={question} />
                </div>
                {question.questionAnswers.length ? <Answers data={question} /> : null}
                <YourAnswer />
              </Wrapper>
              <SideContent />
            </ContentSection>
          </Container>
        </>
      )}
    </>
  );
};

const ContentSection = styled.section`
  width: 100%;
  display: flex;
  @media screen and (max-width: 1279px) {
    display: grid;
    grid-template-columns: calc(100% - 17rem) 17rem;
  }
  @media screen and (max-width: 979px) {
    grid-template-columns: 1fr;
  }
`;

const Wrapper = styled.div`
  width: 51rem;
  height: fit-content;
  padding: 1rem;
  padding-left: 1.5rem;
  padding-right: 2rem;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  .question_content {
    position: relative;
    width: 100%;
  }
  @media screen and (max-width: 1279px) {
    width: 100%;
  }
  @media screen and (max-width: 979px) {
    padding-right: 1.5rem;
  }
`;

export default QuestionDetail;
