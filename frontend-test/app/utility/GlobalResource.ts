type Env = "dev" | "prod";

interface Endpoints {
    GET_SERVICES:   string;
    CREATE_POD:     string;
    DELETE_POD:     string;
    SET_RESPONSE:   string;
  }

const CONFIG = {
        GET_SERVICES: import.meta.env.VITE_API_GET_SERVICES_URI,
        CREATE_POD:   import.meta.env.VITE_API_CREATE_TEST_RESOURCE,
        DELETE_POD:   import.meta.env.VITE_API_DELETE_TEST_RESOURCE,
        SET_RESPONSE: import.meta.env.VITE_API_SET_RESPONSE_URI
};


export default class GlobalResource {
    
    static get GET_SERVICES_URI(): string {
      return CONFIG.GET_SERVICES;
    }

    static get CREATE_TEST_POD_URI(): string {
      return CONFIG.CREATE_POD;
    }

    static get DELETE_TEST_POD_URI(): string {
      return CONFIG.DELETE_POD;
    }

    static get SET_RESPONSE_URI(): string {
        return CONFIG.SET_RESPONSE;
      }
  }
  