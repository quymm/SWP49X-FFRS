import axios from 'axios';

import {courses} from '../data/dummy-data';


axios.defaults.baseURL = 'url-backend-api';
class SearchAPI {
  constructor(){
    
  }

  async searchCourseByTeacher(name) {
    return await courses.filter(course => {
      const { first, last } = course.teacher.name;
      return `${first} ${last}`.toLocaleLowerCase().indexOf(name);
    });  
  }

  async searchCourseByLanguage(language) {
    return await courses.filter(course => !course.language.toLocaleLowerCase().indexOf(language));
  }
}

export { SearchAPI };

export const getListComingLesson = () => {
  return [
    {
      title: 'Academic English IELTS Band 6.0 - 6.5',
      lessonName: 'Section 1 - How to introduce yourself',
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',

      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
     
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
  
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
    
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
    
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
    
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
     
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },
    {
      title: 'Lesson 1',
    
      teacher: {
        picture: {
          "large": "https://randomuser.me/api/portraits/women/22.jpg",
          "medium": "https://randomuser.me/api/portraits/med/women/22.jpg",
          "thumbnail": "https://randomuser.me/api/portraits/thumb/women/22.jpg"
        },
        name: {
          title: "mr",
          first: "jacob",
          last: "wilson"
        }
      }
    },

  ];
}