import baseURL from 'api/baseURL';
// import axios from 'axios';
import { useState } from 'react';
import { FaRegBookmark, FaBookmark } from 'react-icons/fa';
import { useSelector } from 'react-redux';
import { toast } from 'react-toastify';

const Bookmark = ({ bookmark, id, type, setIsShowModal }) => {
  const [isBookmarked, setIsBookmarked] = useState(bookmark);

  const { user } = useSelector(state => state.auth);
  // const user = JSON.parse(localStorage.getItem('user'));

  const handleBookmarkClick = async () => {
    // ! 로그인 안했을 시 모달창 띄우기
    if (!user) {
      setIsShowModal(true);
      return;
    }

    // if (isBookmarked) {
    //   setIsBookmarked(cur => !cur);
    //   toast.success('북마크가 헤제되었습니다!');
    // } else {
    //   setIsBookmarked(cur => !cur);
    //   toast.success('북마크가 체크되었습니다!');
    // }

    // const headers = {
    //   Authorization: `Bearer ${user.authorization}`,
    //   refresh: `Bearer ${user.refresh}`,
    //   'Content-Type': 'Application/json',
    // };
    let data;
    if (type === 'questions') {
      data = {
        memberId: user.memberId,
        questionId: id,
      };
    } else {
      data = {
        memberId: user.memberId,
        answerId: id,
      };
    }
    await baseURL
      .post(`bookmarks/${type}/${id}`, {
        ...data,
      })
      .then(resp => {
        setIsBookmarked(resp.data.questionBookmarkFlag);
        if (resp.data.questionBookmarkFlag) {
          toast.success('북마크가 등록되었습니다!');
        } else {
          toast.success('북마크가 해제되었습니다!');
        }
      })
      .catch(err => {
        console.log(err.message);
      });
    // ! 서버 연동시 사용할 코드
    // await axios({
    //   url: `bookmarks/${type}/${id}`,
    //   method: 'post',
    //   data,
    //   withCredentials: true,
    //   headers,
    // })
    //   .then(resp => {
    //     setIsBookmarked(resp.data.questionBookmarkFlag);
    //     if (resp.data.questionBookmarkFlag) {
    //       toast.success('북마크가 등록되었습니다!');
    //     } else {
    //       toast.success('북마크가 해제되었습니다!');
    //     }
    //   })
    //   .catch(error => {
    //     console.log(error);
    //   });
  };

  return (
    <div>
      {isBookmarked ? (
        <FaBookmark className="bookmark bookmark_true" onClick={handleBookmarkClick} />
      ) : (
        <FaRegBookmark className="bookmark bookmark_false" onClick={handleBookmarkClick} />
      )}
    </div>
  );
};

export default Bookmark;
