type Env = "dev" | "prod";

interface Endpoints {
    GET_SERVICES:   string;
    CREATE_POD:     string;
    DELETE_POD:     string;
    SET_RESPONSE:   string;
  }

const CONFIG = new Map<Env, Endpoints>([
    ["dev",{
        GET_SERVICES: import.meta.env.VITE_API_GET_SERVICES_URI,
        CREATE_POD:   import.meta.env.VITE_API_CREATE_TEST_RESOURCE,
        DELETE_POD:   import.meta.env.VITE_API_DELETE_TEST_RESOURCE,
        SET_RESPONSE: import.meta.env.VITE_API_SET_RESPONSE_URI
      }],
    ["prod", {
        GET_SERVICES: import.meta.env.VITE_API_GET_SERVICES_URI,
        CREATE_POD:   import.meta.env.VITE_API_CREATE_TEST_RESOURCE,
        DELETE_POD:   import.meta.env.VITE_API_DELETE_TEST_RESOURCE,
        SET_RESPONSE: import.meta.env.VITE_API_SET_RESPONSE_URI
      }],
]);


export default class GlobalResource {
    private static readonly ENV: Env = import.meta.env.DEV ? "dev" : "prod";
  
    static get GET_SERVICES_URI(): string {
      return CONFIG.get(GlobalResource.ENV)!.GET_SERVICES;
    }

    static get CREATE_TEST_POD_URI(): string {
      return CONFIG.get(GlobalResource.ENV)!.CREATE_POD;
    }

    static get DELETE_TEST_POD_URI(): string {
      return CONFIG.get(GlobalResource.ENV)!.DELETE_POD;
    }

    static get SET_RESPONSE_URI(): string {
        return CONFIG.get(GlobalResource.ENV)!.SET_RESPONSE;
      }
  }
  