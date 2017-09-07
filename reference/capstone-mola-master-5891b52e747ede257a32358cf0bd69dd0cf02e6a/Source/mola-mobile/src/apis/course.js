import mola from './mola-api';
import qs from 'qs';

import { AsyncStorage } from 'react-native'

class CourseAPI {
    constructor() {

    }
    async getCourseRegisted() {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get('api/course/registed', {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }

    async getCourseByTeacher() {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get('/api/teacher/course', {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async getChapterByCourse(courseId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`/api/teacher/course/${courseId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }
    async addChapter(chapter) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/chapter`, qs.stringify(chapter), {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }
    async editChapter(chapter) {

        console.log("chapter", chapter);
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/chapter/edit`, qs.stringify(chapter), {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }
    async deleteChapter(id) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.delete(`/api/chapter/${id}`, {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });
        return data;
    }
    async createCourse(course) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/course`, qs.stringify(course), {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }
    async editCourse(course) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/course/edit`, qs.stringify(course), {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }

    async deleteCourse(id) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.delete(`/api/course/${id}`, {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });
        return data;
    }

    async updateListLesson(listLesson) {

        const token = await AsyncStorage.getItem('USER_TOKEN');
        mola.defaults.headers.post['Content-Type'] = 'application/json';
        const { data } = await mola.post(`/api/lesson`, listLesson, {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });

        return data;
    }
    async createLesson(lesson) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post('/api/lesson', lesson, {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });
        return data;
    }

    async updateLesson(lesson) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.put('/api/lesson', lesson, {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });
        return data;
    }
    async deleteLesson(id) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.delete(`/api/lesson/${id}`, {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });
        return data;
    }

    // async enrollCourse({courseId, username}) {
    //     const token = await AsyncStorage.getItem('USER_TOKEN');
    //     const { data } = await mola.post(`/api/course/registed`,
    //         qs.stringify({courseId, username}), {
    //         headers: {
    //             'Authorization': token,
    //         }
    //     }).then(result => {
    //         return result;
    //     });
    //     return data;
    // }

    async enrollCourse({courseId, username}) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/course/enroll`,
            qs.stringify({courseId, username}), {
            headers: {
                'Authorization': token,
            }
        }).then(result => {
            return result;
        });
        return data;
    }
    async checkUserEnrollInCourse({courseId, username}) {
        const { data } = await mola.get(`/api/course/enroll?username=${username}&courseId=${courseId}`)
        .then(result => {
            return result;
        });
        return data;
    }
    

    
}

export default CourseAPI;