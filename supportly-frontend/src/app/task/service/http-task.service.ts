import {Injectable} from '@angular/core';
import {Task} from '../Task';
import {BaseApiService} from '../../config/base-api.service';
import {TaskCriteria} from '../TaskCriteria';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpTaskService extends BaseApiService<TaskCriteria, Task> {
  protected resourcePath = 'task';

  updateDone(task: Task): Observable<Task> {
    return this.http.put<Task>(this.buildUrl(`/${this.resourcePath}/update/done`), task);
  }

  getTaskForEmployee(individualId: number | null): Observable<Task[]> {
    return this.http.get<Task[]>(this.buildUrl(`/${this.resourcePath}/${individualId}`));
  }
}

