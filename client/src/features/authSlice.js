import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import authService from './authService';

//로컬스토리지에서 유저정보 가져오기
const user = JSON.parse(localStorage.getItem('user'));
//로컬스토리지에 유저가 있으면 로그인한 걸로 보고 유저를 초기값으로 담고, 아니면 null 로 두기
const initialState = {
  user: user ? user : null,
  isError: false,
  isSuccess: false,
  isLoding: false,
  message: '',
};

//회원가입 슬라이스
export const register = createAsyncThunk('auth/register', async (user, thunkAPI) => {
  try {
    return await authService.register(user);
    //여기서 http 요청을 비동기로 실행
  } catch (error) {
    const message =
      (error.response && error.response.data && error.response.data.message) ||
      error.message ||
      error.toStirng();
    return thunkAPI.rejectWithValue(message);
    //에러가 발생시, 에러메세지를 action.payload에 담아줌.
  }
});

//
export const login = createAsyncThunk('auth/login', async (user, thunkAPI) => {
  try {
    return await authService.login(user);
    //여기서 http 요청을 비동기로 실행
  } catch (error) {
    const message =
      (error.response && error.response.data && error.response.data.message) ||
      error.message ||
      error.toStirng();
    return thunkAPI.rejectWithValue(message);
    //에러가 발생시, 에러메세지를 action.payload에 담아줌.
  }
});

export const logout = createAsyncThunk('auth/logout', async () => {
  await authService.logout();
});

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    reset: state => {
      state.isLoding = false;
      state.isError = false;
      state.isSuccess = false;
      state.message = '';
    },
  },
  extraReducers: builder => {
    builder
      .addCase(register.pending, state => {
        state.isLoding = true;
      })
      .addCase(register.fulfilled, (state, action) => {
        state.isLoding = false;
        state.isSuccess = true;
        state.user = action.payload;
      })
      .addCase(register.rejected, (state, action) => {
        state.isLoding = false;
        state.isError = true;
        state.message = action.payload;
        state.user = null;
      })
      .addCase(login.pending, state => {
        state.isLoding = true;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.isLoding = false;
        state.isSuccess = true;
        state.user = action.payload;
      })
      .addCase(login.rejected, (state, action) => {
        state.isLoding = false;
        state.isError = true;
        state.message = action.payload;
        state.user = null;
      })
      .addCase(logout.fulfilled, state => {
        state.user = null;
      });
  },
});

export const { reset } = authSlice.actions;
export default authSlice.reducer;
